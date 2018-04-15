package com.example.vladimir.easyenglishlearn;

import java.util.ArrayList;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import android.widget.Toast;

public class WordSelection extends Activity implements OnClickListener, OnCheckedChangeListener{
    TextView tvNameTheme, textView1;
    ListView lvWordSelection;
    Button btnStart;
    ArrayAdapter<Word> adapter;
    ArrayList<Word>  tempList;
    ArrayList<Word> selectedItems;
    CheckBox chkAll;
    DialogFragment dialFragmStart;
    DatabaseHelper db;
    Cursor cursor = null;
    float fSize;
    int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_selection);

        db = new DatabaseHelper(this);
        db.open();

        dialFragmStart = new DialogStartUpragn();
        lvWordSelection = (ListView) findViewById(R.id.lvVyborSlov);
        lvWordSelection.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        tvNameTheme = (TextView) findViewById(R.id.tvNameTema);
        textView1 = (TextView) findViewById(R.id.textView1);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);
        chkAll = (CheckBox) findViewById(R.id.chkAll);
        chkAll.setOnCheckedChangeListener(this);

        Intent intent = getIntent();
        String nameOfCategory=intent.getStringExtra("nameOfCategory");
        tvNameTheme.setText(nameOfCategory);

        String selection = "name_of_category == ?";
        String[] selectionArgs = new String[] { nameOfCategory };
        cursor = db.mDB.query(DatabaseHelper.DATABASE_TABLE_WORDS, null, selection, selectionArgs, null, null, null);
        tempList = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Word word = new Word(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LEXEME)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TRANSLATION)));
                    tempList.add(word);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } else
            tempList.add(new Word(" ", " "));

        adapter = new ArrayAdapter<Word>(this, android.R.layout.simple_list_item_multiple_choice, tempList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextSize(fSize);
                textView.setTextColor(color);
                return textView;
            }
        };
        lvWordSelection.setAdapter(adapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        fSize = MainActivity.fSize;
        tvNameTheme.setTextSize(fSize);
        btnStart.setTextSize(fSize);
        chkAll.setTextSize(fSize);
        textView1.setTextSize(fSize);

        color = MainActivity.color;
        tvNameTheme.setTextColor(color);
        btnStart.setTextColor(color);
        chkAll.setTextColor(color);
        textView1.setTextColor(color);
    }

    public void onClick(View v) {
        if (lvWordSelection.getCheckedItemCount() > 3) {
            SparseBooleanArray checked = lvWordSelection.getCheckedItemPositions();
            selectedItems = new ArrayList<>();
            for (int i = 0; i < checked.size(); i++) {
                int position = checked.keyAt(i);
                if (checked.valueAt(i))
                    selectedItems.add(tempList.get(position));
            }
            dialFragmStart.show(getFragmentManager(), "dialFragmStart2");
        } else {
            Toast.makeText(getApplicationContext(), "Выберите минимум 4 слова", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        chkAll = (CheckBox) buttonView;
        int itemCount = lvWordSelection.getCount();
        for(int i=0 ; i < itemCount ; i++){
            lvWordSelection.setItemChecked(i, chkAll.isChecked());
        }
    }
}

