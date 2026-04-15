package com.volodymyr_x.easyenglishlearn.ui.category_edit

import com.volodymyr_x.easyenglishlearn.model.Word

data class CategoryEditState(
    val oldCategoryName: String = "",
    val wordIndex: Int = -1,
    val categoryName: String = "",
    val lexeme: String = "",
    val translation: String = "",
    val words: List<Word> = emptyList()
)
