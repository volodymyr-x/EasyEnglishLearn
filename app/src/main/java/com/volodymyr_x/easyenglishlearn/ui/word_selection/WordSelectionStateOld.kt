package com.volodymyr_x.easyenglishlearn.ui.word_selection

import com.volodymyr_x.easyenglishlearn.ui.model.WordUI

sealed class WordSelectionStateOld {
    object IdleStateOld : WordSelectionStateOld()
    object ShowMessage : WordSelectionStateOld()
    class OpenDialog(val categoryName: String) : WordSelectionStateOld()
    class StartExercise(val dto: WordSelectionDto) : WordSelectionStateOld()
    class UpdateWords(val words: List<WordUI>, val isChooseAllChecked: Boolean = false) :
        WordSelectionStateOld()
}
