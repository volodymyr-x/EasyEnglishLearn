package com.volodymyr_x.easyenglishlearn.ui.category_edit

import com.volodymyr_x.easyenglishlearn.model.Word

sealed class CategoryEditAction {
    data class SaveCategory(val categoryName: String) : CategoryEditAction()
    data class AddWord(val categoryName: String, val lexeme: String, val translation: String) :
        CategoryEditAction()

    data class RemoveWord(val word: Word) : CategoryEditAction()
}
