package com.vladimir_x.easyenglishlearn

import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import androidx.lifecycle.ViewModel
import com.vladimir_x.easyenglishlearn.category_edit.CategoryEditViewModel
import com.vladimir_x.easyenglishlearn.word_selection.WordSelectionViewModel
import com.vladimir_x.easyenglishlearn.ModelFactory
import java.lang.IllegalArgumentException
import java.util.HashMap

class ModelFactory private constructor(private val mCategoryName: String) : NewInstanceFactory() {
    private var mViewModel: ViewModel? = null

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == CategoryEditViewModel::class.java) {
            if (mViewModel == null || mViewModel!!.javaClass != CategoryEditViewModel::class.java) {
                mViewModel = CategoryEditViewModel(mCategoryName)
            }
            return mViewModel as T
        } else if (modelClass == WordSelectionViewModel::class.java) {
            if (mViewModel == null || mViewModel!!.javaClass != WordSelectionViewModel::class.java) {
                mViewModel = WordSelectionViewModel(mCategoryName)
            }
            return mViewModel as T
        }
        throw IllegalArgumentException("Wrong ViewModel class")
    }

    companion object {
        private val sFactoriesMap: MutableMap<String, ModelFactory> = HashMap()
        fun getInstance(categoryName: String): ModelFactory? {
            if (!sFactoriesMap.containsKey(categoryName)) {
                sFactoriesMap[categoryName] = ModelFactory(categoryName)
            }
            return sFactoriesMap[categoryName]
        }
    }
}