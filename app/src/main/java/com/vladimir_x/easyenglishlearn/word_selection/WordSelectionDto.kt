package com.vladimir_x.easyenglishlearn.word_selection

import com.vladimir_x.easyenglishlearn.Constants.Exercises
import com.vladimir_x.easyenglishlearn.model.Word
import java.util.*

class WordSelectionDto(
    val isTranslationDirection: Boolean,
    val selectedWordList: ArrayList<Word>,
    @field:Exercises @param:Exercises val exercise: String
)