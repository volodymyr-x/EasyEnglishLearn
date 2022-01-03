package com.vladimir_x.easyenglishlearn.model

class Answer(
    private val question: Word?,
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