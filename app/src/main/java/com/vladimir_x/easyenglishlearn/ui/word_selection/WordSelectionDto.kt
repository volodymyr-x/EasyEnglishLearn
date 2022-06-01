package com.vladimir_x.easyenglishlearn.ui.word_selection

import com.vladimir_x.easyenglishlearn.Constants.Exercises
import com.vladimir_x.easyenglishlearn.model.Word
import java.util.*

data class WordSelectionDto(
    val isTranslationDirection: Boolean,
    val selectedWordList: ArrayList<Word>,
    @Exercises val exercise: String
)