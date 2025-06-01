package com.volodymyr_x.easyenglishlearn.ui.category_edit

sealed class CategoryEditState {
    object IdleState : CategoryEditState()
    object CloseScreenState : CategoryEditState()
    class ShowMessage(val message: String) : CategoryEditState()
    class CurrentWord(val pair: Pair<String, String>) : CategoryEditState()
}
