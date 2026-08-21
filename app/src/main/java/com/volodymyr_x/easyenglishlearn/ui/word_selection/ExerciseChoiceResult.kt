package com.volodymyr_x.easyenglishlearn.ui.word_selection

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseChoiceResult(
    val isWordToTranslation: Boolean,
    val exerciseType: ExerciseType
)
