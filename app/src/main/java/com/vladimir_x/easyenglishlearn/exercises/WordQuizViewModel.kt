package com.vladimir_x.easyenglishlearn.exercises

import androidx.lifecycle.LiveData
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.SingleLiveEvent
import com.vladimir_x.easyenglishlearn.model.Word
import java.util.*

class WordQuizViewModel : ExerciseViewModel() {
    private val _clearRadioGroupLiveData: SingleLiveEvent<Unit>
    var answers: List<String> = emptyList()

    val clearRadioGroupLiveData: LiveData<Unit?>
        get() = _clearRadioGroupLiveData

    init {
        _clearRadioGroupLiveData = SingleLiveEvent()
    }

    fun onAnswerChecked(answer: String?) {
        answerBuilder.append(answer)
        checkAnswer()
    }

    override fun prepareQuestionAndAnswers() {
        super.prepareQuestionAndAnswers()
        val answerList: MutableList<String> = ArrayList()
        addAnswer(answerList, currentWord)
        while (answerList.size < Constants.ANSWERS_COUNT) {
            val randomIndex = Random().nextInt(wordList.size)
            val tempWord: Word = wordList[randomIndex]
            if (isWordNotInAnswerList(answerList, tempWord)) addAnswer(answerList, tempWord)
        }
        answerList.shuffle()
        answers = answerList
    }

    override fun cleanPreviousData() {
        super.cleanPreviousData()
        _clearRadioGroupLiveData.call()
    }

    private fun isWordNotInAnswerList(answerList: List<String?>, word: Word): Boolean {
        return !answerList.contains(if (translationDirection) word.translation else word.lexeme)
    }

    private fun addAnswer(answerList: MutableList<String>, word: Word?) {
        word?.let {
            if (translationDirection) it.translation else it.lexeme?.let { it1 -> answerList.add(it1) }
        }
    }
}