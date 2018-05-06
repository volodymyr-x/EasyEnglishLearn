package com.example.vladimir.easyenglishlearn;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vladimir.easyenglishlearn.db.DatabaseHelper;
import com.example.vladimir.easyenglishlearn.model.Word;
import com.example.vladimir.easyenglishlearn.utils.ToastUtil;

import java.util.ArrayList;

public class NewCategoryActivity extends AppCompatActivity implements OnClickListener {

    private Button btnCreate, btnSaveWord, btnClean;
    private EditText etCategoryName, etLexeme, etTranslation;
    private TextView tvLexeme, tvTranslation;
    private DatabaseHelper dbHelper;
    private final int MENU_REMOVE_CATEGORY_ID = 103;
    private AdapterContextMenuInfo contextMenuAdapter;
    private float fontSize;
    private ArrayList<Word> wordArrayList;
    private ArrayAdapter<Word> adapter;
    private int indexInArrayList = -1;

    public static final String CATEGORY_NEW_NAME = "CATEGORY_NEW_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_category);

        dbHelper = new DatabaseHelper(this);
        dbHelper.open();

        fontSize = MainActivity.fontSize;

        tvLexeme = findViewById(R.id.nca_tv_lexeme);
        tvTranslation = findViewById(R.id.nca_tv_translation);
        etCategoryName = findViewById(R.id.nca_et_category_name);
        etLexeme = findViewById(R.id.nca_et_lexeme);
        etTranslation = findViewById(R.id.nca_et_translation);
        btnCreate = findViewById(R.id.nca_btn_create);
        btnCreate.setOnClickListener(this);
        btnSaveWord = findViewById(R.id.nca_btn_save_word);
        btnSaveWord.setOnClickListener(this);
        btnClean = findViewById(R.id.nca_btn_clean);
        btnClean.setOnClickListener(this);
        wordArrayList = new ArrayList<>();

        ListView lvCategory = findViewById(R.id.nca_lv_category);
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
        lvCategory.setOnItemClickListener((parent, view, position, id) -> {
            Word word = adapter.getItem(position);
            if (word != null) {
                etLexeme.setText(word.getLexeme());
                etTranslation.setText(word.getTranslation());
            }
            indexInArrayList = position;
            btnSaveWord.setText(getString(R.string.btn_save_word_replace));
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, MENU_REMOVE_CATEGORY_ID, 0, getString(R.string.context_menu_remove));
        contextMenuAdapter =(AdapterView.AdapterContextMenuInfo) menuInfo;
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
            default: break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        etCategoryName.setTextSize(fontSize);
        etLexeme.setTextSize(fontSize);
        etTranslation.setTextSize(fontSize);
        btnSaveWord.setTextSize(fontSize);
        btnCreate.setTextSize(fontSize);
        btnClean.setTextSize(fontSize);
        tvTranslation.setTextSize(fontSize);
        tvLexeme.setTextSize(fontSize);
    }
    @Override
    public void onClick(View v) {
        ToastUtil toastUtil = new ToastUtil(this);
        switch (v.getId()) {
            case R.id.nca_btn_create:
            if (!TextUtils.isEmpty(etCategoryName.getText())) {
                String categoryName = etCategoryName.getText().toString();
                for (Word word: wordArrayList) {
                    dbHelper.addRecWords(categoryName, word.getLexeme(), word.getTranslation());
                }
                Intent intent = new Intent();
                intent.putExtra(CATEGORY_NEW_NAME, categoryName);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                toastUtil.showMessage(R.string.eca_toast_save_edit_category);
            }
            break;
            case R.id.nca_btn_save_word:
                if (isTextFieldsNotEmpty()) {
                    if (indexInArrayList == -1) {
                        wordArrayList.add(newWord());
                    } else {
                        wordArrayList.set(indexInArrayList, newWord());
                        indexInArrayList = -1;
                        btnSaveWord.setText(getString(R.string.eca_save_word));
                    }
                    adapter.notifyDataSetChanged();
                    cleanTextFields();
                } else {
                    toastUtil.showMessage(R.string.eca_toast_save_word_empty_fields);
                }
                break;
            case R.id.nca_btn_clean:
                cleanTextFields();
                indexInArrayList = -1;
                btnSaveWord.setText(getString(R.string.eca_edit_category_clean));
                break;
            default: break;
        }
    }

    private boolean isTextFieldsNotEmpty() {
        boolean result = false;
        if (!TextUtils.isEmpty(etCategoryName.getText()) &
                !TextUtils.isEmpty(etLexeme.getText()) &
                !TextUtils.isEmpty(etTranslation.getText())) {
            result = true;
        } return result;
    }

    private void cleanTextFields() {
        etLexeme.setText("");
        etTranslation.setText("");
    }

    private Word newWord() {
        return new Word(etLexeme.getText().toString(),
                etTranslation.getText().toString());
    }

    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

//    private void showMessage(@StringRes int id) {
//        Toast.makeText(this, getString(id), Toast.LENGTH_SHORT).show();
//    }
}
