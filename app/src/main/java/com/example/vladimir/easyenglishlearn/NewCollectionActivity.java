package com.example.vladimir.easyenglishlearn;

/**
 * Created by BOBAH on 26.03.2015.
 */
import android.content.Intent;
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

public class NewCollectionActivity extends FragmentActivity implements OnClickListener {
    Button btnCrtNewColl, btnNewCollSaveWord, btnNewCollClean;
    EditText etNewCollName, etNewCollLexeme, etNewCollTranslation;
    TextView tvNewCollLexeme, tvNewCollTranslation;
    ListView lvNewColl;
    DatabaseHelper db;
    String nameOfCategory;
    final int MENU_DELCOLL_ID = 103;
    AdapterView.AdapterContextMenuInfo acmi;
    float fSize;
    int color;
    ArrayList<Word> wordArrayList;
    ArrayAdapter<Word> adapter;
    int indexInArrayList = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_coll);

        db = new DatabaseHelper(this);
        db.open();

        tvNewCollLexeme = (TextView) findViewById(R.id.tvNewCollSlovo);
        tvNewCollTranslation = (TextView) findViewById(R.id.tvNewCollPerevod);
        etNewCollName = (EditText) findViewById(R.id.etNewCollName);
        etNewCollLexeme = (EditText) findViewById(R.id.etNewCollLexema);
        etNewCollTranslation = (EditText) findViewById(R.id.etNewCollPerevod);
        btnCrtNewColl = (Button) findViewById(R.id.btnCrtNewColl);
        btnCrtNewColl.setOnClickListener(this);
        btnNewCollSaveWord = (Button) findViewById(R.id.btnNewCollSaveWord);
        btnNewCollSaveWord.setOnClickListener(this);
        btnNewCollClean = (Button) findViewById(R.id.btnNewCollClean);
        btnNewCollClean.setOnClickListener(this);
        wordArrayList = new ArrayList<>();

        lvNewColl = (ListView) findViewById(R.id.lvNewColl);
        adapter = new ArrayAdapter<Word>(this, android.R.layout.simple_list_item_1, wordArrayList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextSize(fSize);
                textView.setTextColor(color);
                return textView;
            }
        };
        lvNewColl.setAdapter(adapter);
        registerForContextMenu(lvNewColl);
        lvNewColl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = adapter.getItem(position);
                etNewCollLexeme.setText(word.lexeme);
                etNewCollTranslation.setText(word.translation);
                indexInArrayList = position;
                btnNewCollSaveWord.setText("Заменить слово");
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, MENU_DELCOLL_ID, 0, "Удалить слово");
        acmi=(AdapterView.AdapterContextMenuInfo) menuInfo;
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
    protected void onResume() {
        super.onResume();
        fSize = MainActivity.fSize;
        etNewCollName.setTextSize(fSize);
        etNewCollLexeme.setTextSize(fSize);
        etNewCollTranslation.setTextSize(fSize);
        btnNewCollSaveWord.setTextSize(fSize);
        btnCrtNewColl.setTextSize(fSize);
        btnNewCollClean.setTextSize(fSize);
        tvNewCollTranslation.setTextSize(fSize);
        tvNewCollLexeme.setTextSize(fSize);

        color = MainActivity.color;
        btnNewCollSaveWord.setTextColor(color);
        btnCrtNewColl.setTextColor(color);
        btnNewCollClean.setTextColor(color);
        tvNewCollTranslation.setTextColor(color);
        tvNewCollLexeme.setTextColor(color);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCrtNewColl:
            if (!TextUtils.isEmpty(etNewCollName.getText())) {
                nameOfCategory = etNewCollName.getText().toString();
                for (Word word: wordArrayList) {
                    db.addRecWords(nameOfCategory, word.lexeme, word.translation);
                }
                Intent intent = new Intent();
                intent.putExtra("nameNewColl", nameOfCategory);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Введите название категории", Toast.LENGTH_SHORT).show();
                return;
            }
            break;
            case R.id.btnNewCollSaveWord:
                if (isTextFieldsNotEmpty()) {
                    if (indexInArrayList == -1) {
                        wordArrayList.add(newWord());
                    } else {
                        wordArrayList.set(indexInArrayList, newWord());
                        indexInArrayList = -1;
                        btnNewCollSaveWord.setText("Сохранить слово");
                    }
                    adapter.notifyDataSetChanged();
                    cleanTextFields();
                } else {
                    Toast.makeText(getApplicationContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnNewCollClean:
                cleanTextFields();
                indexInArrayList = -1;
                btnNewCollSaveWord.setText("Сохранить слово");
                break;
        }
    }

    private boolean isTextFieldsNotEmpty() {
        boolean result = false;
        if (!TextUtils.isEmpty(etNewCollName.getText()) &
                !TextUtils.isEmpty(etNewCollLexeme.getText()) &
                !TextUtils.isEmpty(etNewCollTranslation.getText())) {
            result = true;
        } return result;
    }

    private void cleanTextFields() {
        etNewCollLexeme.setText("");
        etNewCollTranslation.setText("");
    }

    private Word newWord() {
        return new Word(etNewCollLexeme.getText().toString(),
                etNewCollTranslation.getText().toString());
    }

    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
