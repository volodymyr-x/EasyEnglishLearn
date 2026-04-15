package com.volodymyr_x.easyenglishlearn.ui.category_edit

import com.volodymyr_x.easyenglishlearn.model.Word

sealed interface CategoryEditEvent {
    object SaveCategory : CategoryEditEvent
    data class RemoveWord(val word: Word) : CategoryEditEvent
    object AddWord : CategoryEditEvent
    data class CategoryNameUpdate(val newValue: String): CategoryEditEvent
    data class LexemeUpdate(val newValue: String): CategoryEditEvent
    data class TranslationUpdate(val newValue: String): CategoryEditEvent
    object CleanFields: CategoryEditEvent
    data class OnWordClick(val word: Word): CategoryEditEvent
}
