package com.volodymyr_x.easyenglishlearn.ui.category_select

sealed interface CategoryEvent {
    data class OnItemClick(val categoryName: String) : CategoryEvent
    data class OnEditClick(val categoryName: String) : CategoryEvent
    data object OnFabClick : CategoryEvent
    data class OnRemoveClick(val categoryName: String) : CategoryEvent

    data class ShowDeleteDialog(val categoryName: String) : CategoryEvent

    data object HideDeleteDialog : CategoryEvent
}
