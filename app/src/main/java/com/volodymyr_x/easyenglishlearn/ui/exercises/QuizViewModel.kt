package com.volodymyr_x.easyenglishlearn.ui.exercises

import androidx.lifecycle.SavedStateHandle
import com.volodymyr_x.easyenglishlearn.ui.exercises.DataDto.QuizDto
import com.volodymyr_x.easyenglishlearn.model.Word
import com.volodymyr_x.easyenglishlearn.ui.model.WordUI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    state: SavedStateHandle
) : ExerciseViewModel(state) {
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

    private fun convertWordToAnswer(word: WordUI): String =
        if (translationDirection) word.translation else word.lexeme
}
