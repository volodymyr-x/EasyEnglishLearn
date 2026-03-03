package com.volodymyr_x.easyenglishlearn.ui.word_selection

import com.volodymyr_x.easyenglishlearn.ui.model.WordUI

data class WordSelectionState(
    val categoryName: String = "",
    val categoryWords: List<WordUI> = emptyList(),
    val isChooseAllChecked: Boolean = false,
    val message: String? = null,
    val openDialogCategoryName: String? = null,
    val startExerciseDto: WordSelectionDto? = null
)
