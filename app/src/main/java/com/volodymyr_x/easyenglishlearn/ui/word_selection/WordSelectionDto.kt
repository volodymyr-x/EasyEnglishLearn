package com.volodymyr_x.easyenglishlearn.ui.word_selection

import com.volodymyr_x.easyenglishlearn.Constants.Exercises
import com.volodymyr_x.easyenglishlearn.ui.model.WordUI

data class WordSelectionDto(
    val isTranslationDirection: Boolean,
    val selectedWordList: ArrayList<WordUI>,
    @Exercises val exercise: String
)
