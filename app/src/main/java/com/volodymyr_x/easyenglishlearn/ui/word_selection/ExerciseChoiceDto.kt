package com.volodymyr_x.easyenglishlearn.ui.word_selection

import com.volodymyr_x.easyenglishlearn.Constants.Exercises
import java.io.Serializable

data class ExerciseChoiceDto(
    val isWordToTranslation: Boolean,
    @Exercises
    val exerciseType: String
) : Serializable
