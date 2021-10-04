package com.vladimir_x.easyenglishlearn;

import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.vladimir_x.easyenglishlearn.R;
import com.vladimir_x.easyenglishlearn.category_edit.CategoryEditFragment;
import com.vladimir_x.easyenglishlearn.category_select.CategoryFragment;
import com.vladimir_x.easyenglishlearn.word_selection.WordSelectionFragment;

import static com.vladimir_x.easyenglishlearn.Constants.ACTION_ABOUT;
import static com.vladimir_x.easyenglishlearn.Constants.ACTION_EDIT_CATEGORY;
import static com.vladimir_x.easyenglishlearn.Constants.ACTION_OPEN_CATEGORY;
import static com.vladimir_x.easyenglishlearn.Constants.EMPTY_STRING;


public class MainActivity extends AppCompatActivity implements CategoryFragment.Callbacks {


    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = CategoryFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
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
            case R.id.main_menu_about:
                doAction(EMPTY_STRING, ACTION_ABOUT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCategorySelected(String categoryName) {
        doAction(categoryName, ACTION_OPEN_CATEGORY);
    }

    @Override
    public void onCategoryEdit(String categoryName) {
        doAction(categoryName, ACTION_EDIT_CATEGORY);
    }

    private void doAction(String categoryName, int actionCode) {
        int containerId = R.id.detail_fragment_container;
        if (findViewById(R.id.detail_fragment_container) == null) {
            containerId = R.id.fragment_container;
        }
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        switch (actionCode) {
            case ACTION_OPEN_CATEGORY:
                transaction.replace(containerId, WordSelectionFragment.newInstance(categoryName));
                break;
            case ACTION_EDIT_CATEGORY:
                transaction.replace(containerId, CategoryEditFragment.newInstance(categoryName));
                break;
            case ACTION_ABOUT:
                transaction.replace(containerId, AboutFragment.newInstance());
                break;
        }
        transaction
                .addToBackStack(null)
                .commit();
    }
}

