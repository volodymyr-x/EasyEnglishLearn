package com.volodymyr_x.easyenglishlearn.ui.word_selection

import com.volodymyr_x.easyenglishlearn.ui.model.WordUI
import kotlinx.serialization.Serializable

@Serializable
data class WordSelectionResult(
    val isLexemeToTranslation: Boolean,
    val selectedWordList: ArrayList<WordUI>,
    val exerciseType: ExerciseType
)
