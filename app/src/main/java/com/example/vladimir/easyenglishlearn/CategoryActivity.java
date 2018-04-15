package com.example.vladimir.easyenglishlearn;

/**
 * Created by BOBAH on 26.03.2015.
 */
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class CategoryActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    ListView lvTheme;
    TextView tvThemeSelection;
    AdapterContextMenuInfo acmi;
    DialogFragment dialogFragment;
    DatabaseHelper db;
    SimpleCursorAdapter scAdapter;
    float fSize;
    int color;
    final int MENU_NEWCOLL_ID = 101;
    final int MENU_EDITCOLL_ID = 102;
    final int MENU_DELCOLL_ID = 103;
    final int REQUEST_CODE_NEWCOLL = 104;
    final int REQUEST_CODE_EDITCOLL = 105;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);

        db = new DatabaseHelper(this);
        db.open();

        String[] from = new String[] {DatabaseHelper.COLUMN_CATEGORY};
        int[] to = new int[] { R.id.tvSqlListView };

        tvThemeSelection = (TextView) findViewById(R.id.tvVyborTemy);
        dialogFragment = new DialogDellColl ();
        lvTheme = (ListView) findViewById(R.id.Tema33);
        registerForContextMenu(lvTheme);
        scAdapter = new SimpleCursorAdapter(this, R.layout.item_category, null, from, to, 0) {
            @Override
            public void setViewText(TextView v, String text) {
                v.setTextSize(fSize);
                v.setTextColor(color);
                super.setViewText(v, text);
            }
        };
        lvTheme.setAdapter(scAdapter);
        lvTheme.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent1 = new Intent(getApplicationContext(), WordSelection.class);
                intent1.putExtra("nameOfCategory", scAdapter.getCursor()
                        .getString(scAdapter.getCursor().getColumnIndex(DatabaseHelper.COLUMN_CATEGORY)));
                startActivity(intent1);
            }
        });
        getSupportLoaderManager().initLoader(0, null, this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        fSize = MainActivity.fSize;
        tvThemeSelection.setTextSize(fSize);

        color = MainActivity.color;
        tvThemeSelection.setTextColor(color);
    }

    public float getfSize() {
        return fSize;
    }

    public int getColor() {
        return color;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, MENU_EDITCOLL_ID, 0, "Редактировать тему");
        menu.add(0, MENU_NEWCOLL_ID, 0, "Добавить новую тему");
        menu.add(0, MENU_DELCOLL_ID, 0, "Удалить тему");
        acmi=(AdapterContextMenuInfo) menuInfo;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case MENU_EDITCOLL_ID:
                intent =new Intent(this, EditCollectionActivity.class);
                intent.putExtra("nameOfCategory", String.valueOf(scAdapter.getCursor()
                        .getString(scAdapter.getCursor().getColumnIndex(DatabaseHelper.COLUMN_CATEGORY))));
                startActivityForResult(intent, REQUEST_CODE_EDITCOLL);
                break;
            case MENU_NEWCOLL_ID:
                intent = new Intent(this, NewCollectionActivity.class);
                startActivityForResult(intent, REQUEST_CODE_NEWCOLL);
                break;
            case MENU_DELCOLL_ID:
                acmi = (AdapterContextMenuInfo) item.getMenuInfo();
                dialogFragment.show(getFragmentManager(), "dialogFragment");
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_EDITCOLL:
                    String newNameColl = data.getStringExtra("newNameColl");
                    String oldNameColl = data.getStringExtra("oldNameColl");
                    if (!newNameColl.equals(oldNameColl)) {
                       db.updRec(newNameColl, scAdapter.getItemId(acmi.position));
                    }
                    break;
                case REQUEST_CODE_NEWCOLL:
                    String nameNewColl = data.getStringExtra("nameNewColl");
                    db.addRec(nameNewColl);
                    break;

                default:
                    break;
            }
            getSupportLoaderManager().getLoader(0).forceLoad();
        }
    }
    protected void onDestroy() {
        super.onDestroy();
        // закрываем подключение при выходе
        db.close();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return new MyCursorLoader(this, db);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor cursor) {
        scAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
    }

    static class MyCursorLoader extends CursorLoader {
        DatabaseHelper db;

        public MyCursorLoader (Context context, DatabaseHelper db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getAllData();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return cursor;
        }
    }
}

