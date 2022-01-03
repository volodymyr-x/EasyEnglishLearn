package com.vladimir_x.easyenglishlearn.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vladimir_x.easyenglishlearn.State
import com.vladimir_x.easyenglishlearn.State.IdleState
import com.vladimir_x.easyenglishlearn.model.Answer
import com.vladimir_x.easyenglishlearn.model.Word

class QuizViewModel : ViewModel() {
    private var answers: List<String> = emptyList()
    private var iteration = 0
    private var errorCount = 0
    private var translationDirection = true
    private val wordList = mutableListOf<Word>()
    private var currentWord: Word? = null
    private var question: String? = null

    private val _exerciseState = MutableLiveData<State>(IdleState)
    val exerciseState: LiveData<State>
        get() = _exerciseState

    private val isExerciseOver: Boolean
        get() = iteration >= wordList.size

    private fun isAnswerCorrect(answer: CharSequence): Boolean =
        Answer(currentWord, StringBuilder(answer), translationDirection).isCorrect

    /**
     * This method must be called from fragment when it is first started
     */
    fun startExercise(wordList: List<Word>, translationDirection: Boolean) {
        this.wordList.addAll(wordList)
        this.translationDirection = translationDirection
        errorCount = 0
        iteration = 0
        prepareQuestionAndAnswers()
    }

    fun onAnswerChecked(answer: CharSequence) {
        if (isAnswerCorrect(answer)) {
            iteration++
            if (isExerciseOver) {
                finishExercise()
            } else {
                prepareQuestionAndAnswers()
            }
        } else {
            errorCount++
            showError()
        }
    }

    private fun prepareQuestionAndAnswers() {
        currentWord = wordList[iteration].also {
            question = if (translationDirection) it.lexeme else it.translation
        }

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

        changeState(
            State.DataState(
                Pair(
                    convertWordToQuestion(currentWord),
                    answers
                )
            )
        )
    }

    private fun changeState(state: State) {
        _exerciseState.value = state
    }

    private fun finishExercise() {
        changeState(State.CompletedState(errorCount))
    }

    private fun showError() {
        changeState(State.ErrorState())
    }

    private fun convertWordToAnswer(word: Word): String {
        return if (translationDirection) word.translation else word.lexeme
    }

    private fun convertWordToQuestion(word: Word?): String {
        return if (translationDirection) word?.lexeme ?: "" else word?.translation ?: ""
    }
}