package com.example.vladimir.easyenglishlearn;

import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vladimir.easyenglishlearn.db.DatabaseHelper;
import com.example.vladimir.easyenglishlearn.db.MyCursorLoader;
import com.example.vladimir.easyenglishlearn.fragments.RemoveCategoryFragment;

public class CategoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView tvCategorySelection;
    private AdapterContextMenuInfo contextMenuAdapter;
    private DialogFragment dialogFragment;
    private DatabaseHelper dbHelper;
    private SimpleCursorAdapter cursorAdapter;
    private float fontSize;
    private static final int MENU_GROUP_ID = 0;
    private final int MENU_NEW_COLL_ID = 101;
    private final int MENU_EDIT_COLL_ID = 102;
    private final int MENU_REMOVE_COLL_ID = 103;
    private final int REQUEST_CODE_NEW_CATEGORY = 104;
    private final int REQUEST_CODE_EDIT_CATEGORY = 105;
    public static final String CATEGORY_NAME = "CATEGORY_NAME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);

        dbHelper = new DatabaseHelper(this);
        dbHelper.open();

        fontSize = MainActivity.fontSize;

        String[] from = new String[] {DatabaseHelper.COLUMN_CATEGORY};
        int[] to = new int[] { R.id.aswc_tv_category};

        tvCategorySelection = findViewById(R.id.ca_tv_category_select);
        dialogFragment = new RemoveCategoryFragment();
        ListView lvCategory = findViewById(R.id.ca_list_view);
        registerForContextMenu(lvCategory);
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.all_saved_words_category, null, from, to, 0) {
            @Override
            public void setViewText(TextView v, String text) {
                super.setViewText(v, text);
                v.setTextSize(fontSize);
            }
        };
        lvCategory.setAdapter(cursorAdapter);
        lvCategory.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), WordSelectionActivity.class);
            intent.putExtra(CATEGORY_NAME, cursorAdapter.getCursor()
                    .getString(cursorAdapter.getCursor().getColumnIndex(DatabaseHelper.COLUMN_CATEGORY)));
            startActivity(intent);
        });
        getSupportLoaderManager().initLoader(0, null, this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        tvCategorySelection.setTextSize(fontSize);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(MENU_GROUP_ID, MENU_EDIT_COLL_ID, 0, getString(R.string.ca_cm_edit_coll));
        menu.add(MENU_GROUP_ID, MENU_NEW_COLL_ID, 1, getString(R.string.ca_cm_new_coll));
        menu.add(MENU_GROUP_ID, MENU_REMOVE_COLL_ID, 2, getString(R.string.ca_cm_remove_coll));
        contextMenuAdapter =(AdapterContextMenuInfo) menuInfo;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case MENU_EDIT_COLL_ID:
                intent = new Intent(this, EditCategoryActivity.class);
                intent.putExtra(CATEGORY_NAME, String.valueOf(cursorAdapter.getCursor()
                        .getString(cursorAdapter.getCursor().getColumnIndex(DatabaseHelper.COLUMN_CATEGORY))));
                startActivityForResult(intent, REQUEST_CODE_EDIT_CATEGORY);
                return true;
            case MENU_NEW_COLL_ID:
                intent = new Intent(this, NewCategoryActivity.class);
                startActivityForResult(intent, REQUEST_CODE_NEW_CATEGORY);
                return true;
            case MENU_REMOVE_COLL_ID:
                contextMenuAdapter = (AdapterContextMenuInfo) item.getMenuInfo();
                dialogFragment.show(getSupportFragmentManager(), RemoveCategoryFragment.DIALOG_REMOVE_CATEGORY);
                return true;
            default: break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_EDIT_CATEGORY:
                    String categoryNewName = data.getStringExtra(EditCategoryActivity.CATEGORY_NEW_NAME);
                    String categoryOldName = data.getStringExtra(EditCategoryActivity.CATEGORY_OLD_NAME);
                    if (!categoryNewName.equals(categoryOldName)) {
                       dbHelper.updRec(categoryNewName, cursorAdapter.getItemId(contextMenuAdapter.position));
                    }
                    break;
                case REQUEST_CODE_NEW_CATEGORY:
                    String nameNewColl = data.getStringExtra(NewCategoryActivity.CATEGORY_NEW_NAME);
                    dbHelper.addRec(nameNewColl);
                    break;
                default:    break;
            }
            getSupportLoaderManager().getLoader(0).forceLoad();
        }
    }
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return new MyCursorLoader(this, dbHelper);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public SimpleCursorAdapter getCursorAdapter() {
        return cursorAdapter;
    }

    public AdapterContextMenuInfo getContextMenuAdapter() {
        return contextMenuAdapter;
    }

    public DatabaseHelper getDbHelper() {
        return dbHelper;
    }

    public float getFontSize() {
        return fontSize;
    }

    //    static class MyCursorLoader extends CursorLoader {
//        DatabaseHelper db_helper;
//
//        public MyCursorLoader (Context context, DatabaseHelper db_helper) {
//            super(context);
//            this.db_helper = db_helper;
//        }
//
//        @Override
//        public Cursor loadInBackground() {
//            Cursor cursor = db_helper.getAllData();
//            try {
//                TimeUnit.SECONDS.sleep(2);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return cursor;
//        }
//    }
}

