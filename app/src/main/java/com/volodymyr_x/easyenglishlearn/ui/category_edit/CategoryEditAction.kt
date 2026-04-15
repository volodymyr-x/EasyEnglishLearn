package com.volodymyr_x.easyenglishlearn.ui.category_edit

sealed interface CategoryEditAction {
    data class ShowMessage(val message: String): CategoryEditAction
    object CloseScreen: CategoryEditAction
}
