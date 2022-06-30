package com.vladimir_x.easyenglishlearn.ui.word_selection

import com.vladimir_x.easyenglishlearn.ui.model.WordUI

sealed class WordSelectionState {
    object IdleState : WordSelectionState()
    object ShowMessage : WordSelectionState()
    class OpenDialog(val categoryName: String) : WordSelectionState()
    class UpdateWords(val words: List<WordUI>) : WordSelectionState()
    class StartExercise(val dto: WordSelectionDto) : WordSelectionState()
}
