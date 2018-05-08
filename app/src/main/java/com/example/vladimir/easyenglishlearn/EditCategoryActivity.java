package com.example.vladimir.easyenglishlearn;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vladimir.easyenglishlearn.db.DatabaseHelper;
import com.example.vladimir.easyenglishlearn.model.Word;
import com.example.vladimir.easyenglishlearn.utils.ToastUtil;

import java.util.ArrayList;

public class EditCategoryActivity extends AppCompatActivity {
    private EditText etCategoryName, etEditLexeme, etEditTranslation;
    private Button btnSaveCategory, btnSaveWord, btnClean;
    private TextView tvLexeme, tvTranslation;
    private DatabaseHelper dbHelper;
    private AdapterContextMenuInfo contextMenuAdapter;
    private String newCategoryName, oldCategoryName;
    private int arrayListIndex = -1;
    private Cursor cursor;
    private ArrayList<Word> wordArrayList;
    private ArrayAdapter<Word> adapter;
    private float fontSize;
    private ToastUtil toastUtil;

    public static final String CATEGORY_NEW_NAME = "CATEGORY_NEW_NAME";
    public static final String CATEGORY_OLD_NAME = "CATEGORY_OLD_NAME";
    private final int MENU_REMOVE_CATEGORY_ID = 113;

    private OnClickListener btnSaveCategoryListener = v -> {
        if (!TextUtils.isEmpty(etCategoryName.getText().toString().trim())) {
            newCategoryName = etCategoryName.getText().toString();
            cursor = dbHelper.getWords(oldCategoryName);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        dbHelper.delRecWords(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORDS_ID)));
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
            for (Word word : wordArrayList) {
                dbHelper.addRecWords(newCategoryName, word.getLexeme(), word.getTranslation());
            }
            Intent intent = new Intent();
            intent.putExtra(CATEGORY_NEW_NAME, newCategoryName);
            intent.putExtra(CATEGORY_OLD_NAME, oldCategoryName);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            toastUtil.showMessage(R.string.eca_toast_save_edit_category);
        }
    };

    private OnClickListener btnSaveWordListener = v -> {
        if (isTextFieldsNotEmpty()) {
            if (arrayListIndex == -1) {
                wordArrayList.add(newWord());
            } else {
                wordArrayList.set(arrayListIndex, newWord());
                arrayListIndex = -1;
                btnSaveWord.setText(getString(R.string.eca_save_word));
            }
            adapter.notifyDataSetChanged();
            cleanTextFields();
        } else {
            toastUtil.showMessage(R.string.eca_toast_save_word_empty_fields);
        }
    };

    private OnClickListener btnCleanListener = v -> {
        cleanTextFields();
        arrayListIndex = -1;
        btnSaveWord.setText(getString(R.string.eca_edit_category_clean));
    };

    private OnItemClickListener lvCategoryListener = (parent, view, position, id) -> {
        Word word = adapter.getItem(position);
        if (word != null) {
            etEditLexeme.setText(word.getLexeme());
            etEditTranslation.setText(word.getTranslation());
            arrayListIndex = position;
            btnSaveWord.setText(getString(R.string.btn_save_word_replace));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_category);

        dbHelper = new DatabaseHelper(this);
        dbHelper.open();

        tvLexeme = findViewById(R.id.eca_tv_lexeme);
        tvTranslation = findViewById(R.id.eca_tv_translation);
        etCategoryName = findViewById(R.id.eca_et_category_name);
        etEditLexeme = findViewById(R.id.eca_et_lexeme);
        etEditTranslation = findViewById(R.id.eca_et_translation);
        btnSaveCategory = findViewById(R.id.eca_btn_save_category);
        btnSaveCategory.setOnClickListener(btnSaveCategoryListener);
        btnSaveWord = findViewById(R.id.eca_btn_save_word);
        btnSaveWord.setOnClickListener(btnSaveWordListener);
        btnClean = findViewById(R.id.eca_btn_clean);
        btnClean.setOnClickListener(btnCleanListener);

        fontSize = MainActivity.fontSize;
        toastUtil = new ToastUtil(this);
        Intent intent = getIntent();
        newCategoryName = intent.getStringExtra(CategoryActivity.CATEGORY_NAME);
        oldCategoryName = newCategoryName;
        etCategoryName.setText(newCategoryName);

        wordArrayList = new ArrayList<>();
        cursor = dbHelper.getWords(newCategoryName);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Word word = new Word(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LEXEME)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TRANSLATION)));
                    wordArrayList.add(word);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        ListView lvCategory = findViewById(R.id.eca_lv_category);
        adapter = new ArrayAdapter<Word>(this, android.R.layout.simple_list_item_1, wordArrayList) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextSize(fontSize);
                return textView;
            }
        };
        lvCategory.setAdapter(adapter);
        registerForContextMenu(lvCategory);
        lvCategory.setOnItemClickListener(lvCategoryListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        etCategoryName.setTextSize(fontSize);
        etEditLexeme.setTextSize(fontSize);
        etEditTranslation.setTextSize(fontSize);
        btnSaveCategory.setTextSize(fontSize);
        btnSaveWord.setTextSize(fontSize);
        btnClean.setTextSize(fontSize);
        tvTranslation.setTextSize(fontSize);
        tvLexeme.setTextSize(fontSize);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_REMOVE_CATEGORY_ID:
                contextMenuAdapter = (AdapterContextMenuInfo) item.getMenuInfo();
                wordArrayList.remove(contextMenuAdapter.position);
                adapter.notifyDataSetChanged();
                cleanTextFields();
                return true;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, MENU_REMOVE_CATEGORY_ID, 0, getString(R.string.context_menu_remove));
        contextMenuAdapter = (AdapterContextMenuInfo) menuInfo;
    }

    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    private boolean isTextFieldsNotEmpty() {
        boolean result = false;
        if (!TextUtils.isEmpty(etCategoryName.getText().toString().trim()) &&
                !TextUtils.isEmpty(etEditLexeme.getText().toString().trim()) &&
                !TextUtils.isEmpty(etEditTranslation.getText().toString().trim())) {
            result = true;
        }
        return result;
    }

    private void cleanTextFields() {
        etEditLexeme.setText("");
        etEditTranslation.setText("");
    }

    private Word newWord() {
        return new Word(etEditLexeme.getText().toString(),
                etEditTranslation.getText().toString());
    }
}