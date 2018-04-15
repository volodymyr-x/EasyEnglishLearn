package com.example.vladimir.easyenglishlearn;

/**
 * Created by BOBAH on 26.03.2015.
 */
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditCollectionActivity extends FragmentActivity implements OnClickListener
{
    EditText etEditCollName, etEditCollLexeme, etEditCollTranslation;
    Button btnSaveEditColl, btnEditCollSaveWord, btnEditCollClean;
    TextView tvEditCollWord, tvEditCollTranslation;
    ListView lvEditColl;
    DatabaseHelper db;
    final int MENU_DELCOLL_ID = 113;
    AdapterView.AdapterContextMenuInfo acmi;
    String nameOfCategory, oldNameOfCategory;
    int indexInArrayList = -1;
    float fSize;
    int color;
    Cursor cursor;
    ArrayList<Word> wordArrayList;
    ArrayAdapter<Word> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_coll);

        db = new DatabaseHelper(this);
        db.open();

        tvEditCollWord = (TextView) findViewById(R.id.tvEditCollSlovo);
        tvEditCollTranslation = (TextView) findViewById(R.id.tvEditCollPerevod);
        etEditCollName = (EditText) findViewById(R.id.etEditCollName);
        etEditCollLexeme = (EditText) findViewById(R.id.etEditCollLexema);
        etEditCollTranslation = (EditText) findViewById(R.id.etEditCollPerevod);
        btnSaveEditColl = (Button) findViewById(R.id.btnSaveEditColl);
        btnSaveEditColl.setOnClickListener(this);
        btnEditCollSaveWord = (Button) findViewById(R.id.btnEditCollSaveWord);
        btnEditCollSaveWord.setOnClickListener(this);
        btnEditCollClean = (Button) findViewById(R.id.btnEditCollClean);
        btnEditCollClean.setOnClickListener(this);

        Intent intent = getIntent();
        nameOfCategory = intent.getStringExtra("nameOfCategory");
        oldNameOfCategory = nameOfCategory;
        etEditCollName.setText(nameOfCategory);

        cursor =  db.getDataWords(nameOfCategory);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                wordArrayList = new ArrayList<>();
                do {
                    Word word = new Word(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LEXEME)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TRANSLATION)));
                    wordArrayList.add(word);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        lvEditColl = (ListView) findViewById(R.id.lvEditColl);
        adapter = new ArrayAdapter<Word>(this, android.R.layout.simple_list_item_1, wordArrayList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextSize(fSize);
                textView.setTextColor(color);
                return textView;
            }
        };
        lvEditColl.setAdapter(adapter);
        registerForContextMenu(lvEditColl);
        lvEditColl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = adapter.getItem(position);
                etEditCollLexeme.setText(word.lexeme);
                etEditCollTranslation.setText(word.translation);
                indexInArrayList = position;
                btnEditCollSaveWord.setText("Заменить слово");
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        fSize = MainActivity.fSize;
        etEditCollName.setTextSize(fSize);
        etEditCollLexeme.setTextSize(fSize);
        etEditCollTranslation.setTextSize(fSize);
        btnSaveEditColl.setTextSize(fSize);
        btnEditCollSaveWord.setTextSize(fSize);
        btnEditCollClean.setTextSize(fSize);
        tvEditCollTranslation.setTextSize(fSize);
        tvEditCollWord.setTextSize(fSize);
        color = MainActivity.color;
//        etEditCollName.setTextColor(color);
//        etEditCollLexeme.setTextColor(color);
//        etEditCollTranslation.setTextColor(color);
        btnSaveEditColl.setTextColor(color);
        btnEditCollSaveWord.setTextColor(color);
        btnEditCollClean.setTextColor(color);
        tvEditCollWord.setTextColor(color);
        tvEditCollTranslation.setTextColor(color);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_DELCOLL_ID:
                acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                wordArrayList.remove(acmi.position);
                adapter.notifyDataSetChanged();
                cleanTextFields();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, MENU_DELCOLL_ID, 0, "Удалить слово");
        acmi=(AdapterView.AdapterContextMenuInfo) menuInfo;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSaveEditColl:
            if (!TextUtils.isEmpty(etEditCollName.getText())) {
                nameOfCategory = etEditCollName.getText().toString();
                cursor =  db.getDataWords(oldNameOfCategory);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            db.delRecWords(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORDS_ID)));
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }
                for (Word word: wordArrayList) {
                    db.addRecWords(nameOfCategory, word.lexeme, word.translation);
                }
                Intent intent = new Intent();
                intent.putExtra("newNameColl", nameOfCategory);
                intent.putExtra("oldNameColl", oldNameOfCategory);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Введите название категории", Toast.LENGTH_SHORT).show();
            } break;
            case R.id.btnEditCollSaveWord:
                if (isTextFieldsNotEmpty()) {
                    if (indexInArrayList == -1) {
                        wordArrayList.add(newWord());
                    } else {
                        wordArrayList.set(indexInArrayList, newWord());
                        indexInArrayList = -1;
                        btnEditCollSaveWord.setText("Сохранить слово");
                    }
                    adapter.notifyDataSetChanged();
                    cleanTextFields();

                } else {
                    Toast.makeText(getApplicationContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                } break;
            case R.id.btnEditCollClean:
                cleanTextFields();
                indexInArrayList = -1;
                btnEditCollSaveWord.setText("Сохранить слово");
                break;
        }

    }

    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private boolean isTextFieldsNotEmpty() {
        boolean result = false;
        if (!TextUtils.isEmpty(etEditCollName.getText()) &
                !TextUtils.isEmpty(etEditCollLexeme.getText()) &
                !TextUtils.isEmpty(etEditCollTranslation.getText())) {
            result = true;
        } return result;
    }

    private void cleanTextFields() {
        etEditCollLexeme.setText("");
        etEditCollTranslation.setText("");
    }

    private Word newWord() {
        return new Word(etEditCollLexeme.getText().toString(),
                etEditCollTranslation.getText().toString());
    }

}