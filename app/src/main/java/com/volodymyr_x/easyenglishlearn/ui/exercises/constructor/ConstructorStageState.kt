package com.volodymyr_x.easyenglishlearn.ui.exercises.constructor
import com.volodymyr_x.easyenglishlearn.ui.model.WordUI

data class ConstructorStageState(
    val iteration: Int = 1,
    val errorCount: Int = 0,
    val question: String = "",
    val currentAnswer: String = "",
    val incorrectAnswer: String = "",
    val isLexemeToTranslation: Boolean = true,
    val currentWord: WordUI? = null,
    val wordList: List<WordUI> = emptyList(),
    val letters: List<String> = emptyList()
)
