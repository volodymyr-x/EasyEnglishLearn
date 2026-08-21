package com.volodymyr_x.easyenglishlearn.navigation

import androidx.navigation3.runtime.NavKey
import com.volodymyr_x.easyenglishlearn.ui.word_selection.WordSelectionResult
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object CategoryList : Route, NavKey

    @Serializable
    data class CategoryEdit(val categoryName: String) : Route, NavKey


    @Serializable
    data object CategoryAdd : Route, NavKey

    @Serializable
    data class WordSelection(val categoryName: String) : Route, NavKey

    @Serializable
    data class ExerciseQuiz(val result: WordSelectionResult) : Route, NavKey

    @Serializable
    data class ExerciseConstructor(val result: WordSelectionResult) : Route, NavKey
}
