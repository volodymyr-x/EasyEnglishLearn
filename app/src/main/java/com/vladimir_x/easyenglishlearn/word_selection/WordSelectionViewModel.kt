package com.vladimir_x.easyenglishlearn.word_selection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vladimir_x.easyenglishlearn.App
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.Constants.Exercises
import com.vladimir_x.easyenglishlearn.R
import com.vladimir_x.easyenglishlearn.SingleLiveEvent
import com.vladimir_x.easyenglishlearn.db.WordDao
import com.vladimir_x.easyenglishlearn.model.Word
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList

class WordSelectionViewModel(val categoryName: String) : ViewModel() {
    private val _messageLiveData: SingleLiveEvent<Int>
    private val _choiceDialogLiveData: SingleLiveEvent<String>
    private val _wordsLiveData: MutableLiveData<List<Word>>
    private val _selectedWordsLiveData: SingleLiveEvent<WordSelectionDto>
    private val _closeDialogLiveData: SingleLiveEvent<Void>
    private val selectedWordList: ArrayList<Word>
    private val compositeDisposable: CompositeDisposable
    private var translationDirection = true

    val messageLiveData: LiveData<Int?>
        get() = _messageLiveData
    val choiceDialogLiveData: LiveData<String?>
        get() = _choiceDialogLiveData
    val wordsLiveData: LiveData<List<Word>>
        get() = _wordsLiveData
    val closeDialogLiveData: LiveData<Void?>
        get() = _closeDialogLiveData
    val selectedWordsLiveData: LiveData<WordSelectionDto?>
        get() = _selectedWordsLiveData

    init {
        val repository = App.instance?.database?.wordDao()
        _selectedWordsLiveData = SingleLiveEvent()
        _messageLiveData = SingleLiveEvent()
        _choiceDialogLiveData = SingleLiveEvent()
        _wordsLiveData = MutableLiveData()
        _closeDialogLiveData = SingleLiveEvent()
        selectedWordList = ArrayList()
        compositeDisposable = CompositeDisposable()
        subscribeWordsToData(repository)
    }

    fun onBtnStartClick() {
        if (selectedWordList.size < Constants.ANSWERS_COUNT) {
            showMessage()
        } else {
            _choiceDialogLiveData.setValue(categoryName)
        }
    }

    fun onChooseAllChange(checked: Boolean) {
        selectedWordList.clear()
        val wordList = _wordsLiveData.value!!
        if (checked) {
            selectedWordList.addAll(wordList)
        }
        for (word in wordList) {
            word.isChecked = checked
        }
        _wordsLiveData.value = wordList
    }

    fun onItemCheckBoxChange(checked: Boolean, word: Word) {
        if (checked) {
            if (!selectedWordList.contains(word)) selectedWordList.add(word)
        } else {
            selectedWordList.remove(word)
        }
        word.isChecked = checked
    }

    fun onBtnConstructorClick() {
        sendDTO(Constants.WORD_CONSTRUCTOR)
    }

    fun onBtnQuizClick() {
        sendDTO(Constants.WORD_QUIZ)
    }

    fun onBtnCancelClick() {
        _closeDialogLiveData.call()
    }

    fun onDirectionChanged(checkedId: Int) {
        when (checkedId) {
            R.id.ecf_rb_en_ru -> translationDirection = true
            R.id.ecf_rb_ru_en -> translationDirection = false
            else -> {
            }
        }
    }

    private fun subscribeWordsToData(repository: WordDao?) {
        val disposable: Disposable = repository!!.getWordsByCategory(
            categoryName
        )
            .subscribeOn(Schedulers.io())
            .subscribe { words: List<Word> -> _wordsLiveData.postValue(words) }
        compositeDisposable.add(disposable)
    }

    private fun sendDTO(@Exercises exerciseType: String) {
        _closeDialogLiveData.call()
        val dto = WordSelectionDto(
            translationDirection,
            selectedWordList, exerciseType
        )
        _selectedWordsLiveData.value = dto
    }

    private fun showMessage() {
        _messageLiveData.value = R.string.wsa_toast_min_words_count
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }
}