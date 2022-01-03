package com.vladimir_x.easyenglishlearn.exercises

import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.exercises.DataDto.ConstructorDto
import java.util.ArrayList

class ConstructorViewModel : ExerciseViewModel() {
    private var answer: String = ""
    private val letterList: MutableList<Char> = ArrayList()


    override fun prepareQuestionAndAnswers() {
        super.prepareQuestionAndAnswers()

        cleanPreviousData()
        splitCurrentWord()
        letterList.shuffle()
        sendData(createDto())
    }

    fun onNewButtonClick(letter: String) {
        answer += letter
        letterList.remove(letter[0])
        if (letterList.isEmpty()) {
            checkAnswer(answer)
        } else {
            sendData(createDto())
        }
    }

    fun onButtonUndoClick() {
        if (answer.isNotEmpty()) {
            val undoLetter = answer[answer.lastIndex]
            answer = answer.substring(0, answer.lastIndex)
            letterList.add(undoLetter)
            sendData(createDto())
        }
    }

    private fun createDto() = ConstructorDto(
        convertWordToQuestion(currentWord),
        answer,
        letterList
    )

    private fun cleanPreviousData() {
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