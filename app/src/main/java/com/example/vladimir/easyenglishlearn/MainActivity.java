package com.example.vladimir.easyenglishlearn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;

import com.example.vladimir.easyenglishlearn.db.DatabaseHelper;
import com.example.vladimir.easyenglishlearn.db.MyCursorLoader;
import com.example.vladimir.easyenglishlearn.fragments.AboutFragment;
import com.example.vladimir.easyenglishlearn.fragments.CategoryFragment;
import com.example.vladimir.easyenglishlearn.fragments.RemoveCategoryFragment;
import com.example.vladimir.easyenglishlearn.preferences.PreferencesActivity;

public class MainActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor>,
        RemoveCategoryFragment.RemoveCategoryListener{

    public static float fontSize;
    private int changeableFragment;
    private DialogFragment dialogFragment;
    private DatabaseHelper dbHelper;
    private SimpleCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        dbHelper = new DatabaseHelper(this);
        dbHelper.open();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        fontSize = Float.parseFloat(prefs.getString(getString(R.string.pr_size_list),
                getString(R.string.pr_default_size)));

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_container, new CategoryFragment())
                        .commit();
            }
            changeableFragment = R.id.fragment_container;
        } else {
            changeableFragment = R.id.second_fragment_container;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_settings:
                // TODO: 12.05.2018 переделать на преференс фрагмент
                startActivity(new Intent(this, PreferencesActivity.class));
                return true;
            case R.id.main_menu_about:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(changeableFragment, new AboutFragment())
                        .addToBackStack(null)
                        .commit();
                return true;
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyCursorLoader(this, dbHelper);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public int getSelectedColumnIndex() {
        return cursorAdapter.getCursor().getColumnIndex(DatabaseHelper.COLUMN_CATEGORY);
    }

    @Override
    public void okBtnClicked() {

    }
}

