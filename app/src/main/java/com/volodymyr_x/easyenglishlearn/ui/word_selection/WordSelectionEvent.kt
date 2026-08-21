package com.volodymyr_x.easyenglishlearn.ui.word_selection

import com.volodymyr_x.easyenglishlearn.ui.model.WordUI

sealed interface WordSelectionEvent {
    data object OnBtnStartClick : WordSelectionEvent
    data class OnItemCheckBoxChange(val word: WordUI) : WordSelectionEvent
    data object OnChooseAllClick : WordSelectionEvent
    data class OnExerciseChoose(val exerciseChoiceResult: ExerciseChoiceResult) : WordSelectionEvent
    data object HideDialog : WordSelectionEvent
}
