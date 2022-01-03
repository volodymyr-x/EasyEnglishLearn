package com.vladimir_x.easyenglishlearn.model

import java.lang.StringBuilder

class Answer(
    private val question: Word?,
    answerBuilder: StringBuilder,
    private val translationDirection: Boolean
) {
    private val answer: String = answerBuilder.toString()
    val isCorrect: Boolean
        get() = if (translationDirection) {
            question?.translation.equals(answer, ignoreCase = true)
        } else {
            question?.lexeme.equals(answer, ignoreCase = true)
        }

}