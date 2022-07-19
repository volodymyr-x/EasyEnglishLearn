package com.vladimir_x.easyenglishlearn.ui.exercises

import androidx.lifecycle.SavedStateHandle
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.ui.exercises.DataDto.ConstructorDto
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class ConstructorViewModel @Inject constructor(
    state: SavedStateHandle
) : ExerciseViewModel(state) {
    private var answer: String = ""
    private val letterList: MutableList<Letter> = ArrayList()

    override fun prepareQuestionAndAnswers() {
        super.prepareQuestionAndAnswers()

        cleanPreviousData()
        splitCurrentWord()
        letterList.shuffle()
        sendData(createDto())
    }

    fun onLetterButtonClick(letter: Letter) {
        answer += letter.value
        letterList.remove(letter)
        if (letterList.isEmpty()) {
            checkAnswer(answer)
        } else {
            sendData(createDto())
        }
    }

    fun onButtonUndoClick() {
        if (answer.isNotEmpty()) {
            val undoLetterValue = answer[answer.lastIndex].toString()
            answer = answer.substring(0, answer.lastIndex)
            letterList.add(Letter(value = undoLetterValue))
            sendData(createDto())
        }
    }

    private fun createDto() = ConstructorDto(
        convertWordToQuestion(currentWord),
        answer,
        ArrayList<Letter>().apply {
            addAll(letterList)
        }
    )

    private fun cleanPreviousData() {
        letterList.clear()
        answer = Constants.EMPTY_STRING
    }

    private fun splitCurrentWord() {
        val letters = if (translationDirection) {
            prepareLetters(currentWord?.translation)
        } else {
            prepareLetters(currentWord?.lexeme)
        }
        letterList.addAll(letters)
    }

    private fun prepareLetters(word: String?) =
        word?.map { Letter(value = it.toString()) } ?: emptyList()
}