package com.example.vladimir.easyenglishlearn;

import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vladimir.easyenglishlearn.db.DatabaseHelper;
import com.example.vladimir.easyenglishlearn.fragments.CategoryFragment;
import com.example.vladimir.easyenglishlearn.fragments.ExerciseChoiceFragment;
import com.example.vladimir.easyenglishlearn.model.Word;
import com.example.vladimir.easyenglishlearn.utils.ToastUtil;

import java.util.ArrayList;

public class WordSelectionActivity extends AppCompatActivity
        implements ExerciseChoiceFragment.ExerciseChoiceListener {

    private TextView tvCategoryName, tvTitle;
    private ListView lvSelectedWords;
    private Button btnStart;
    private ArrayList<Word> allSavedWordsList;
    private ArrayList<Word> selectedWordsList;
    private CheckBox cbChooseAll;
    private DialogFragment dialogFragment;
    private float fontSize;
    private ToastUtil toastUtil;

    public static final String EXERCISE_CHOICE_FRAGMENT = "EXERCISE_CHOICE_FRAGMENT";
    public static final String SELECTED_WORDS = "SELECTED_WORDS";
    public static final String TRANSLATION_DIRECTION = "TRANSLATION_DIRECTION";

    private OnClickListener btnStartListener = v -> {
        if (lvSelectedWords.getCheckedItemCount() > 3) {
            SparseBooleanArray checked = lvSelectedWords.getCheckedItemPositions();
            selectedWordsList = new ArrayList<>();
            for (int i = 0; i < checked.size(); i++) {
                int position = checked.keyAt(i);
                if (checked.valueAt(i))
                    selectedWordsList.add(allSavedWordsList.get(position));
            }
            dialogFragment.show(getSupportFragmentManager(), EXERCISE_CHOICE_FRAGMENT);
        } else {
            toastUtil.showMessage(R.string.wsa_toast_min_words_count);
        }
    };

    private OnCheckedChangeListener cbChooseAllListener = (buttonView, isChecked) -> {
        cbChooseAll = (CheckBox) buttonView;
        int itemCount = lvSelectedWords.getCount();
        for(int i=0 ; i < itemCount ; i++){
            lvSelectedWords.setItemChecked(i, cbChooseAll.isChecked());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_selection);

        DatabaseHelper db = new DatabaseHelper(this);
        db.open();

        toastUtil = new ToastUtil(this);
        dialogFragment = new ExerciseChoiceFragment();

        lvSelectedWords = findViewById(R.id.wsa_lv_words_choice);
        lvSelectedWords.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        tvCategoryName = findViewById(R.id.wsa_tv_category_name);
        tvTitle = findViewById(R.id.wsa_tv_title);
        btnStart = findViewById(R.id.wsa_btn_start);
        btnStart.setOnClickListener(btnStartListener);
        cbChooseAll = findViewById(R.id.wsa_cb_choose_all);
        cbChooseAll.setOnCheckedChangeListener(cbChooseAllListener);

        Intent intent = getIntent();
        String categoryName = intent.getStringExtra(CategoryFragment.CATEGORY_NAME);
        tvCategoryName.setText(categoryName);

        String selection = "name_of_category == ?";
        String[] selectionArgs = new String[] { categoryName };
        Cursor cursor = db.sqLiteDB.query(DatabaseHelper.DATABASE_TABLE_WORDS, null, selection, selectionArgs, null, null, null);
        allSavedWordsList = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Word word = new Word(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LEXEME)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TRANSLATION)));
                    allSavedWordsList.add(word);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } else
            allSavedWordsList.add(new Word(" ", " "));

        ArrayAdapter<Word> adapter = new ArrayAdapter<Word>(this, android.R.layout.simple_list_item_multiple_choice, allSavedWordsList) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextSize(fontSize);
                return textView;
            }
        };
        lvSelectedWords.setAdapter(adapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        fontSize = MainActivity.fontSize;
        tvCategoryName.setTextSize(fontSize);
        btnStart.setTextSize(fontSize);
        cbChooseAll.setTextSize(fontSize);
        tvTitle.setTextSize(fontSize);
    }

    @Override
    public void btnConstructorClicked(boolean translationDirection) {
        startActivity(getPreparedIntent(WordConstructorActivity.class, translationDirection));
    }

    @Override
    public void btnQuizClicked(boolean translationDirection) {
        startActivity(getPreparedIntent(WordQuizActivity.class, translationDirection));
    }

    private Intent getPreparedIntent (Class clazz, boolean translationDirection) {
        Intent intent = new Intent(this, clazz);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(SELECTED_WORDS, selectedWordsList);
        bundle.putBoolean(TRANSLATION_DIRECTION, translationDirection);
        intent.putExtras(bundle);
        return intent;
    }
}

