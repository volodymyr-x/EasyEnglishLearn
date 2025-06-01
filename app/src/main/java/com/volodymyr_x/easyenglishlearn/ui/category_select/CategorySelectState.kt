package com.volodymyr_x.easyenglishlearn.ui.category_select

sealed class CategorySelectState {
    object IdleState : CategorySelectState()

    class EditCategory(val data: String) : CategorySelectState()

    class RemoveCategory(val data: String) : CategorySelectState()

    class OpenCategory(val data: String) : CategorySelectState()
}
