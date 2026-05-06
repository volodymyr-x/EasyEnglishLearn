package com.volodymyr_x.easyenglishlearn.model

import com.volodymyr_x.easyenglishlearn.ui.model.WordUI

class Answer(
    private val question: WordUI?,
    private val answer: String,
    private val isLexemeToTranslation: Boolean
) {
    val isCorrect: Boolean
        get() = if (isLexemeToTranslation) {
            question?.translation.equals(answer, ignoreCase = true)
        } else {
            question?.lexeme.equals(answer, ignoreCase = true)
        }
}
