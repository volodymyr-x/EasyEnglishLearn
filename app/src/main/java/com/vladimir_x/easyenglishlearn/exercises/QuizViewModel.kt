package com.vladimir_x.easyenglishlearn.exercises

import com.vladimir_x.easyenglishlearn.exercises.DataDto.QuizDto
import com.vladimir_x.easyenglishlearn.model.Word

class QuizViewModel : ExerciseViewModel() {
    private var answers: List<String> = emptyList()


    override fun prepareQuestionAndAnswers() {
        super.prepareQuestionAndAnswers()

        val answerList = wordList
            .filter { it != currentWord }
            .shuffled()
            .take(2)
            .toMutableList()
            .also { list ->
                currentWord?.let { list.add(it) }
            }

        answers = answerList
            .map { convertWordToAnswer(it) }
            .shuffled()

        sendData(
            QuizDto(
                convertWordToQuestion(currentWord),
                answers
            )
        )
    }

    fun onAnswerChecked(answer: CharSequence) {
        checkAnswer(answer)
    }

    private fun convertWordToAnswer(word: Word): String =
        if (translationDirection) word.translation else word.lexeme
}