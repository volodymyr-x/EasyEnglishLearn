package com.vladimir_x.easyenglishlearn.ui.word_selection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladimir_x.easyenglishlearn.App
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.Constants.Exercises
import com.vladimir_x.easyenglishlearn.R
import com.vladimir_x.easyenglishlearn.util.SingleLiveEvent
import com.vladimir_x.easyenglishlearn.data.db.WordDao
import com.vladimir_x.easyenglishlearn.data.repository.WordsRepositoryImpl
import com.vladimir_x.easyenglishlearn.domain.WordsInteractor
import com.vladimir_x.easyenglishlearn.domain.WordsInteractorImpl
import com.vladimir_x.easyenglishlearn.model.Word
import kotlinx.coroutines.launch
import java.util.ArrayList

class WordSelectionViewModel(
    private val wordsInteractor: WordsInteractor = WordsInteractorImpl(WordsRepositoryImpl(App.instance.database.wordDao()))
    ) : ViewModel() {
    private val _messageLiveData: SingleLiveEvent<Int> = SingleLiveEvent()
    private val _choiceDialogLiveData: SingleLiveEvent<String> = SingleLiveEvent()
    private val _wordsLiveData: MutableLiveData<List<Word>> = MutableLiveData()
    private val _selectedWordsLiveData: SingleLiveEvent<WordSelectionDto> = SingleLiveEvent()
    private val _closeDialogLiveData: SingleLiveEvent<Void> = SingleLiveEvent()
    private val selectedWordList: ArrayList<Word> = arrayListOf()
    private var translationDirection = true
    var categoryName = ""

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

    fun init(categoryName: String?) {
        //val repository = App.instance?.database?.wordDao()
        categoryName?.let {
            this.categoryName = categoryName
            subscribeWordsToData()
        }
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
        val wordList = _wordsLiveData.value ?: emptyList()
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

    fun onBtnConstructorClick(isFromEnglish: Boolean) {
        sendDTO(Constants.WORD_CONSTRUCTOR, isFromEnglish)
    }

    fun onBtnQuizClick(isFromEnglish: Boolean) {
        sendDTO(Constants.WORD_QUIZ, isFromEnglish)
    }

    fun onBtnCancelClick() {
        _closeDialogLiveData.call()
    }

    fun onDirectionChanged(checkedId: Int) {
        translationDirection = when (checkedId) {
            R.id.ecf_rb_en_ru -> true
            else -> false
        }
    }

    private fun subscribeWordsToData() {
        viewModelScope.launch {
            _wordsLiveData.value = wordsInteractor.getWordsByCategory(categoryName)
        }
    }

    private fun sendDTO(@Exercises exerciseType: String, isFromEnglish: Boolean) {
        val dto = WordSelectionDto(
            isFromEnglish,
            selectedWordList, exerciseType
        )
        translationDirection = isFromEnglish
        _selectedWordsLiveData.value = dto
        _closeDialogLiveData.call()
    }

    private fun showMessage() {
        _messageLiveData.value = R.string.wsa_toast_min_words_count
    }
}