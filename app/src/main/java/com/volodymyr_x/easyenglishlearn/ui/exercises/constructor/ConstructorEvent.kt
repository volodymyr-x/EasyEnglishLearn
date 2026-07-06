package com.volodymyr_x.easyenglishlearn.ui.exercises.constructor

sealed class ConstructorEvent {
    data class LetterButtonClicked(val letter: String) : ConstructorEvent()
    data object UndoButtonClicked : ConstructorEvent()
}
