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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vladimir.easyenglishlearn.db.DatabaseHelper;
import com.example.vladimir.easyenglishlearn.fragments.ExerciseChoiceFragment;
import com.example.vladimir.easyenglishlearn.model.Word;
import com.example.vladimir.easyenglishlearn.utils.ToastUtil;

import java.util.ArrayList;

public class WordSelectionActivity extends AppCompatActivity implements OnClickListener, OnCheckedChangeListener{

    private TextView tvCategoryName, tvTitle;
    private ListView lvSelectedWords;
    private Button btnStart;
    private ArrayList<Word> allSavedWordsList;
    private ArrayList<Word> selectedWordsList;
    private CheckBox cbChooseAll;
    private DialogFragment dialogFragment;
    private float fontSize;

    public static final String EXERCISE_CHOICE_FRAGMENT = "EXERCISE_CHOICE_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_selection);

        DatabaseHelper db = new DatabaseHelper(this);
        db.open();

        dialogFragment = new ExerciseChoiceFragment();
        lvSelectedWords = findViewById(R.id.wsa_lv_words_choice);
        lvSelectedWords.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        tvCategoryName = findViewById(R.id.wsa_tv_category_name);
        tvTitle = findViewById(R.id.wsa_tv_title);
        btnStart = findViewById(R.id.wsa_btn_start);
        btnStart.setOnClickListener(this);
        cbChooseAll = findViewById(R.id.wsa_cb_choose_all);
        cbChooseAll.setOnCheckedChangeListener(this);

        Intent intent = getIntent();
        String categoryName = intent.getStringExtra(CategoryActivity.CATEGORY_NAME);
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

    public void onClick(View v) {
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
            new ToastUtil(this).showMessage(R.string.wsa_toast_min_words_count);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        cbChooseAll = (CheckBox) buttonView;
        int itemCount = lvSelectedWords.getCount();
        for(int i=0 ; i < itemCount ; i++){
            lvSelectedWords.setItemChecked(i, cbChooseAll.isChecked());
        }
    }

    public ArrayList<Word> getSelectedWordsList() {
        return selectedWordsList;
    }
}

