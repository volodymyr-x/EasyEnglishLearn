package com.vladimir_x.easyenglishlearn

import com.vladimir_x.easyenglishlearn.category_select.CategoryFragment.Companion.newInstance
import com.vladimir_x.easyenglishlearn.word_selection.WordSelectionFragment.Companion.newInstance
import com.vladimir_x.easyenglishlearn.category_edit.CategoryEditFragment.Companion.newInstance
import com.vladimir_x.easyenglishlearn.AboutFragment.Companion.newInstance
import androidx.appcompat.app.AppCompatActivity
import androidx.annotation.LayoutRes
import com.vladimir_x.easyenglishlearn.R
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.vladimir_x.easyenglishlearn.category_select.CategoryFragment
import com.vladimir_x.easyenglishlearn.word_selection.WordSelectionFragment
import com.vladimir_x.easyenglishlearn.category_edit.CategoryEditFragment
import com.vladimir_x.easyenglishlearn.AboutFragment

class MainActivity : AppCompatActivity(), CategoryFragment.Callbacks {
    @get:LayoutRes
    private val layoutResId: Int
        get() = R.layout.activity_masterdetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)
        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fragment_container)
        if (fragment == null) {
            fragment = CategoryFragment.newInstance()
            fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.main_menu_about -> {
                doAction(
                    Constants.EMPTY_STRING,
                    Constants.ACTION_ABOUT
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCategorySelected(categoryName: String?) {
        doAction(categoryName, Constants.ACTION_OPEN_CATEGORY)
    }

    override fun onCategoryEdit(categoryName: String?) {
        doAction(categoryName, Constants.ACTION_EDIT_CATEGORY)
    }

    private fun doAction(categoryName: String?, actionCode: Int) {
        var containerId = R.id.detail_fragment_container
        if (findViewById<View?>(R.id.detail_fragment_container) == null) {
            containerId = R.id.fragment_container
        }
        val transaction = supportFragmentManager
            .beginTransaction()
        when (actionCode) {
            Constants.ACTION_OPEN_CATEGORY -> transaction.replace(
                containerId,
                WordSelectionFragment.newInstance(categoryName)
            )
            Constants.ACTION_EDIT_CATEGORY -> transaction.replace(
                containerId,
                CategoryEditFragment.newInstance(categoryName)
            )
            Constants.ACTION_ABOUT -> transaction.replace(containerId, AboutFragment.newInstance())
        }
        transaction
            .addToBackStack(null)
            .commit()
    }
}