package com.volodymyr_x.easyenglishlearn.ui.word_selection

import com.volodymyr_x.easyenglishlearn.ui.model.WordUI

sealed class WordSelectionAction {
    data object OnBtnStartClick : WordSelectionAction()
    data class OnItemCheckBoxChange(val word: WordUI) : WordSelectionAction()
    data object OnChooseAllClick : WordSelectionAction()
}
