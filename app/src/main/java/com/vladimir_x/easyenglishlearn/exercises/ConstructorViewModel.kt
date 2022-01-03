package com.vladimir_x.easyenglishlearn.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.State
import com.vladimir_x.easyenglishlearn.model.Answer
import com.vladimir_x.easyenglishlearn.model.Word
import java.util.*

class ConstructorViewModel : ViewModel() {
    private var iteration = 0
    private var errorCount = 0
    private var translationDirection = true
    private val wordList = mutableListOf<Word>()
    private var currentWord: Word? = null
    private var question: String = ""
    private var answer: String = ""

    private val _exerciseState = MutableLiveData<State>(State.IdleState)
    val exerciseState: LiveData<State>
        get() = _exerciseState

    private val isExerciseOver: Boolean
        get() = iteration >= wordList.size

    private fun isAnswerCorrect(answer: CharSequence): Boolean =
        Answer(currentWord, StringBuilder(answer), translationDirection).isCorrect

    private val letterList: MutableList<Char> = ArrayList()


    fun onNewButtonClick(letter: String) {
        answer += letter
        letterList.remove(letter[0])
        if (letterList.isEmpty()) {
            checkAnswer()
        } else {
            changeState(
                State.DataState(
                    Triple(
                        convertWordToQuestion(currentWord),
                        answer,
                        letterList
                    )
                )
            )
        }
    }

    /**
     * This method must be called from fragment  when it is first started
     */
    fun startExercise(wordList: List<Word>, translationDirection: Boolean) {
        this.wordList.addAll(wordList)
        this.translationDirection = translationDirection
        errorCount = 0
        iteration = 0
        prepareQuestionAndAnswers()
    }

    fun onButtonUndoClick() {
        if (answer.isNotEmpty()) {
            val undoLetter = answer[answer.lastIndex]
            answer = answer.substring(0, answer.lastIndex)
            letterList.add(undoLetter)
            changeState(
                State.DataState(
                    Triple(
                        convertWordToQuestion(currentWord),
                        answer,
                        letterList
                    )
                )
            )
        }
    }

    private fun checkAnswer() {
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
            prepareQuestionAndAnswers()
        }
    }

    private fun prepareQuestionAndAnswers() {
        cleanPreviousData()
        currentWord = wordList[iteration].also {
            question = if (translationDirection) it.lexeme else it.translation
        }


        splitCurrentWord()
        letterList.shuffle()
        changeState(
            State.DataState(
                Triple(
                    convertWordToQuestion(currentWord),
                    answer,
                    letterList
                )
            )
        )
    }

    private fun cleanPreviousData() {
        letterList.clear()
        answer = Constants.EMPTY_STRING
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

    private fun splitCurrentWord() {
        val letters = if (translationDirection) {
            currentWord?.translation?.toCharArray()
        } else {
            currentWord?.lexeme?.toCharArray()
        }
        letters?.let {
            for (letter in letters) {
                letterList.add(letter)
            }
        }
    }

    private fun convertWordToQuestion(word: Word?): String {
        return if (translationDirection) word?.lexeme ?: "" else word?.translation ?: ""
    }
}