package com.vladimir_x.easyenglishlearn.ui.word_selection

import com.vladimir_x.easyenglishlearn.Constants.Exercises
import java.io.Serializable

data class ExerciseChoiceDto(
    val isTranslationDirection: Boolean,
    @Exercises
    val exercise: String
) : Serializable