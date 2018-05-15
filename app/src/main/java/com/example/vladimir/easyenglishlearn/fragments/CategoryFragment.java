package com.example.vladimir.easyenglishlearn.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vladimir.easyenglishlearn.EditCategoryActivity;
import com.example.vladimir.easyenglishlearn.R;
import com.example.vladimir.easyenglishlearn.WordSelectionActivity;
import com.example.vladimir.easyenglishlearn.db.DatabaseHelper;
import com.example.vladimir.easyenglishlearn.db.MyCursorLoader;
import com.example.vladimir.easyenglishlearn.fragments.RemoveCategoryFragment;

import static com.example.vladimir.easyenglishlearn.EditCategoryActivity.*;

public class CategoryFragment extends ListFragment/* implements LoaderCallbacks<Cursor>,
        RemoveCategoryFragment.RemoveCategoryListener*/ {

    private View title;
    private TextView tvCategorySelection;
    private AdapterContextMenuInfo contextMenuAdapter;
    private DialogFragment dialogFragment;
    //private DatabaseHelper dbHelper;
    private SimpleCursorAdapter cursorAdapter;
    private CategoryFragmentListener listener;
    private float fontSize = 20;// TODO: 12.05.2018 убрать хардкод
    private static final int MENU_GROUP_ID = 0;
    private final int MENU_NEW_COLL_ID = 101;
    private final int MENU_EDIT_COLL_ID = 102;
    private final int MENU_REMOVE_COLL_ID = 103;
    private final int REQUEST_CODE_NEW_CATEGORY = 104;
    private final int REQUEST_CODE_EDIT_CATEGORY = 105;
    public static final String CATEGORY_NAME = "CATEGORY_NAME";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (CategoryFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement CategoryFragmentListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        dbHelper = new DatabaseHelper(getActivity());
//        dbHelper.open();
        dialogFragment = new RemoveCategoryFragment();

        title = inflater.inflate(R.layout.lv_title, container, false);
        tvCategorySelection = title.findViewById(R.id.cf_lv_title);
        String[] from = new String[] {DatabaseHelper.COLUMN_CATEGORY};
        int[] to = new int[] { R.id.aswc_tv_category};

        cursorAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.all_saved_words_category, null, from, to, 0) {
            @Override
            public void setViewText(TextView v, String text) {
                super.setViewText(v, text);
                v.setTextSize(fontSize);
            }
        };
        //getActivity().getSupportLoaderManager().initLoader(0, null, this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        float fontSize = Float.parseFloat(prefs.getString(getString(R.string.pr_size_list),
                getString(R.string.pr_default_size)));
        tvCategorySelection.setTextSize(fontSize);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(null);
        getListView().addHeaderView(title);
        setListAdapter(cursorAdapter);
        registerForContextMenu(getListView());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (listener != null) {
            listener.itemClicked(position);
        }
        Intent intent = new Intent(getActivity(), WordSelectionActivity.class);
        intent.putExtra(CATEGORY_NAME, cursorAdapter.getCursor()
                .getString(cursorAdapter.getCursor().getColumnIndex(DatabaseHelper.COLUMN_CATEGORY)));
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(MENU_GROUP_ID, MENU_EDIT_COLL_ID, 0, getString(R.string.ca_cm_edit_category));
        menu.add(MENU_GROUP_ID, MENU_NEW_COLL_ID, 1, getString(R.string.ca_cm_new_category));
        menu.add(MENU_GROUP_ID, MENU_REMOVE_COLL_ID, 2, getString(R.string.ca_cm_remove_category));
        contextMenuAdapter =(AdapterContextMenuInfo) menuInfo;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case MENU_EDIT_COLL_ID:
                intent = new Intent(getActivity(), EditCategoryActivity.class);
//                intent.putExtra(CATEGORY_NAME,
//                        cursorAdapter.getCursor().getString(getSelectedColumnIndex()));
                //startActivityForResult(intent, REQUEST_CODE_EDIT_CATEGORY);
                startActivity(intent);
                return true;
            case MENU_NEW_COLL_ID:
                intent = new Intent(getActivity(), EditCategoryActivity.class);
                //startActivityForResult(intent, REQUEST_CODE_NEW_CATEGORY);
                startActivity(intent);
                return true;
            case MENU_REMOVE_COLL_ID:
                contextMenuAdapter = (AdapterContextMenuInfo) item.getMenuInfo();
                dialogFragment.show(getActivity().getSupportFragmentManager(),
                        RemoveCategoryFragment.DIALOG_REMOVE_CATEGORY);
                return true;
            default: break;
        }
        return super.onContextItemSelected(item);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            String categoryNewName;
//            switch (requestCode) {
//                case REQUEST_CODE_EDIT_CATEGORY:
//                    categoryNewName = data.getStringExtra(CATEGORY_NEW_NAME);
//                    String categoryOldName = data.getStringExtra(CATEGORY_OLD_NAME);
//                    if (!categoryNewName.equals(categoryOldName)) {
//                       dbHelper.updRec(categoryNewName,
//                               cursorAdapter.getItemId(contextMenuAdapter.position));
//                    }
//                    break;
//                case REQUEST_CODE_NEW_CATEGORY:
//                    categoryNewName = data.getStringExtra(CATEGORY_NEW_NAME);
//                    dbHelper.addRec(categoryNewName);
//                    break;
//                default:    break;
//            }
//            getSupportLoaderManager().getLoader(0).forceLoad();
//        }
//    }

//    public void onDestroy() {
//        super.onDestroy();
//        dbHelper.close();
//    }

//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
//        return new MyCursorLoader(getActivity(), dbHelper);
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
//        cursorAdapter.swapCursor(cursor);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//    }

//    public int getSelectedColumnIndex() {
//        return cursorAdapter.getCursor().getColumnIndex(DatabaseHelper.COLUMN_CATEGORY);
//    }

//    @Override
//    public void okBtnClicked() {
//        String categoryName = cursorAdapter.getCursor().getString(getSelectedColumnIndex());
//        String selection = "name_of_category == ?";
//        String[] selectionArgs = new String[] { categoryName };
//        Cursor cursor = dbHelper.sqLiteDB.query(DatabaseHelper.DATABASE_TABLE_WORDS, null,
//                selection, selectionArgs, null, null, null);
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                do {
//                    dbHelper.delRecWords(cursor.getLong(getSelectedColumnIndex()));
//
//                } while (cursor.moveToNext());
//            }
//            cursor.close();
//        }
//        dbHelper.delRec(contextMenuAdapter.id);
//        getActivity().getSupportLoaderManager().getLoader(0).forceLoad();
//    }

    public interface CategoryFragmentListener {
        void itemClicked(int position);
    }
}

