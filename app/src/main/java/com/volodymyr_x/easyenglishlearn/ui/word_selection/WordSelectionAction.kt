package com.volodymyr_x.easyenglishlearn.ui.word_selection

sealed interface WordSelectionAction {
    object ShowMessage : WordSelectionAction
    class StartExercise(val dto: WordSelectionDto) : WordSelectionAction
}
