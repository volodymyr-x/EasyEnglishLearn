package com.volodymyr_x.easyenglishlearn.ui.exercises.quiz

import com.volodymyr_x.easyenglishlearn.ui.model.WordUI

data class QuizStageState(
    val iteration: Int = 1,
    val errorCount: Int = 0,
    val question: String = "",
    val isLexemeToTranslation: Boolean = true,
    val currentWord: WordUI? = null,
    val wordList: List<WordUI> = emptyList(),
    val answers: List<String> = emptyList()
)
