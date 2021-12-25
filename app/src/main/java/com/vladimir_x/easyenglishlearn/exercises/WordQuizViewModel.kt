package com.vladimir_x.easyenglishlearn.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vladimir_x.easyenglishlearn.SingleLiveEvent
import com.vladimir_x.easyenglishlearn.model.Word

class WordQuizViewModel : ExerciseViewModel() {
    private val _clearRadioGroupLiveData: SingleLiveEvent<Unit> = SingleLiveEvent()
    val clearRadioGroupLiveData: LiveData<Unit?>
        get() = _clearRadioGroupLiveData

    private val _questionLiveData = MutableLiveData<Pair<String, List<String>>>()
    val questionLiveData: LiveData<Pair<String, List<String>>>
        get() = _questionLiveData

    private var answers: List<String> = emptyList()

    fun onAnswerChecked(answerIndex: Int) {
        answerBuilder.append(answers[answerIndex])
        checkAnswer()
    }

    override fun prepareQuestionAndAnswers() {
        super.prepareQuestionAndAnswers()
        val answerList = wordList
            .filter { it != currentWord }
            .take(2)
            .toMutableList()
        currentWord?.let { answerList.add(it) }
        answers = answerList.map { convertWordToAnswer(it) }.shuffled()

        _questionLiveData.value = Pair(
            convertWordToQuestion(currentWord),
            answers
        )
    }

    override fun cleanPreviousData() {
        super.cleanPreviousData()
        _clearRadioGroupLiveData.call()
    }

    private fun convertWordToAnswer(word: Word): String {
        return if (translationDirection) word.translation else word.lexeme
    }

    private fun convertWordToQuestion(word: Word?): String {
        return if (translationDirection) word?.lexeme ?: "" else word?.translation ?: ""
    }
}