package com.volodymyr_x.easyenglishlearn.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.volodymyr_x.easyenglishlearn.Constants
import com.volodymyr_x.easyenglishlearn.R
import com.volodymyr_x.easyenglishlearn.navigation.NavigationRoot
import com.volodymyr_x.easyenglishlearn.ui.category_edit.CategoryEditFragment
import com.volodymyr_x.easyenglishlearn.ui.category_select.CategoryFragment
import com.volodymyr_x.easyenglishlearn.ui.theme.AppTheme
import com.volodymyr_x.easyenglishlearn.ui.word_selection.WordSelectionFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CategoryFragment.Callbacks {
    @get:LayoutRes
    private val layoutResId: Int
        get() = R.layout.activity_masterdetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                NavigationRoot(
                    showMessageAction = { message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    override fun onCategorySelected(categoryName: String?) {
        doAction(categoryName, Constants.ACTION_OPEN_CATEGORY)
    }

    override fun onCategoryEdit(categoryName: String?) {
        doAction(categoryName, Constants.ACTION_EDIT_CATEGORY)
    }

    override fun onCategoryAdd() {
        doAction(null, Constants.ACTION_ADD_CATEGORY)
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
            Constants.ACTION_ADD_CATEGORY -> transaction.replace(
                containerId,
                CategoryEditFragment.newInstance()
            )
        }
        transaction
            .addToBackStack(null)
            .commit()
    }
}
