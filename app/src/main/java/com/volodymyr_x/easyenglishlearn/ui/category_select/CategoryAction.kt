package com.volodymyr_x.easyenglishlearn.ui.category_select

sealed class CategoryAction {
    data class Selected(val categoryName: String) : CategoryAction()
    data class Edit(val categoryName: String) : CategoryAction()
     data class Remove(val categoryName: String) : CategoryAction()

    data object CreateNew : CategoryAction()
}
