package com.volodymyr_x.easyenglishlearn.ui.category_select

sealed interface CategoryAction {
    data class Selected(val categoryName: String) : CategoryAction
    data class Edit(val categoryName: String) : CategoryAction
    data object CreateNew : CategoryAction
    data class Removed(val categoryName: String) : CategoryAction
}
