package com.volodymyr_x.easyenglishlearn.model

import com.volodymyr_x.easyenglishlearn.ui.model.WordUI

class Answer(
    private val question: WordUI?,
    answer: CharSequence,
    private val translationDirection: Boolean
) {
    private val answer: String = answer.toString()
    val isCorrect: Boolean
        get() = if (translationDirection) {
            question?.translation.equals(answer, ignoreCase = true)
        } else {
            question?.lexeme.equals(answer, ignoreCase = true)
        }

}
