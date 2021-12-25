package com.vladimir_x.easyenglishlearn.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.vladimir_x.easyenglishlearn.SingleLiveEvent
import com.vladimir_x.easyenglishlearn.model.Answer
import com.vladimir_x.easyenglishlearn.model.Word
import java.lang.StringBuilder

abstract class ExerciseViewModel : ViewModel() {
    private val _exerciseCloseLiveData: SingleLiveEvent<Unit> = SingleLiveEvent()
    private val _messageLiveData: SingleLiveEvent<Int> = SingleLiveEvent()
    private var iteration = 0
    private var errorCount = 0
    var translationDirection = true
    var wordList: List<Word> = listOf()
    var answerBuilder: StringBuilder = StringBuilder()
    var currentWord: Word? = null
    var question: String? = null

    val exerciseCloseLiveData: LiveData<Unit?>
        get() = _exerciseCloseLiveData
    val messageLiveData: LiveData<Int?>
        get() = _messageLiveData

    private val isExerciseOver: Boolean
        get() {
            val answer = Answer(currentWord, answerBuilder, translationDirection)
            return if (answer.isCorrect) {
                ++iteration >= wordList.size
            } else {
                showMessage(-1)
                errorCount++
                false
            }
        }

    /**
     * This method must be called from fragment  when it is first started
     */
    fun startExercise(wordList: List<Word>, translationDirection: Boolean) {
        this.wordList = wordList
        this.translationDirection = translationDirection
        errorCount = 0
        iteration = 0
        prepareQuestionAndAnswers()
    }

    open fun prepareQuestionAndAnswers() {
        currentWord = wordList[iteration]
        currentWord?.let {
            question = if (translationDirection) it.lexeme else it.translation
            cleanPreviousData()
        }
    }

    fun checkAnswer() {
        if (isExerciseOver) {
            finishExercise()
        } else {
            prepareQuestionAndAnswers()
        }
    }

    open fun cleanPreviousData() {
        answerBuilder.delete(0, answerBuilder.length)
    }

    private fun finishExercise() {
        showMessage(errorCount)
        _exerciseCloseLiveData.call()
    }

    private fun showMessage(errorsCount: Int) {
        _messageLiveData.value = errorsCount
    }
}