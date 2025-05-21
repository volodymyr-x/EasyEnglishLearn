package com.vladimir_x.easyenglishlearn.ui.word_selection

import com.vladimir_x.easyenglishlearn.Constants.Exercises
import com.vladimir_x.easyenglishlearn.model.Word
import com.vladimir_x.easyenglishlearn.ui.model.WordUI
import java.util.*

data class WordSelectionDto(
    val isTranslationDirection: Boolean,
    val selectedWordList: ArrayList<WordUI>,
    @Exercises val exercise: String
)