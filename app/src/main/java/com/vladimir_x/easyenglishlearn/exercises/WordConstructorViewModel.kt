package com.vladimir_x.easyenglishlearn.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vladimir_x.easyenglishlearn.Constants
import java.util.*

class WordConstructorViewModel : ExerciseViewModel() {
    private val _charArrayLiveData = MutableLiveData<List<Char>>()
    private val letterList: MutableList<Char> = ArrayList()
    var answer: String = ""

    val charArrayLiveData: LiveData<List<Char>>
        get() = _charArrayLiveData

    fun onNewButtonClick(letter: String) {
        answerBuilder.append(letter)
        answer = answerBuilder.toString()
        letterList.remove(Character.valueOf(letter[0]))
        if (letterList.isEmpty()) {
            checkAnswer()
        } else {
            _charArrayLiveData.value = letterList
        }
    }

    fun onButtonUndoClick() {
        if (answerBuilder.isNotEmpty()) {
            val lastCharIndex = answerBuilder.lastIndex
            val letterFromButton = answerBuilder[lastCharIndex]
            answerBuilder.deleteCharAt(lastCharIndex)
            answer = answerBuilder.toString()
            letterList.add(letterFromButton)
            _charArrayLiveData.value = letterList
        }
    }

    override fun prepareQuestionAndAnswers() {
        super.prepareQuestionAndAnswers()
        splitCurrentWord()
        letterList.shuffle()
        _charArrayLiveData.value = letterList
    }

    override fun cleanPreviousData() {
        super.cleanPreviousData()
        letterList.clear()
        answer = Constants.EMPTY_STRING
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
}