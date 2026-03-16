package com.volodymyr_x.easyenglishlearn.ui.category_select

data class CategorySelectState(
    val categoryList: List<String> = emptyList(),
    val showDeleteDialog: Boolean = false,
    val selectedCategoryName: String = ""
)
