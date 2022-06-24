package com.vladimir_x.easyenglishlearn.ui.word_selection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.domain.WordsInteractor
import com.vladimir_x.easyenglishlearn.model.Word
import com.vladimir_x.easyenglishlearn.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordSelectionViewModel @Inject constructor(
    state: SavedStateHandle,
    private val wordsInteractor: WordsInteractor
) : ViewModel() {
    private val _messageLiveData: SingleLiveEvent<Unit> = SingleLiveEvent()
    private val _choiceDialogLiveData: SingleLiveEvent<String> = SingleLiveEvent()
    private val _wordsLiveData: MutableLiveData<List<Word>> = MutableLiveData()
    private val _selectedWordsLiveData: SingleLiveEvent<WordSelectionDto> = SingleLiveEvent()
    private val selectedWordList: ArrayList<Word> = arrayListOf()
    var categoryName = ""

    val messageLiveData: LiveData<Unit?>
        get() = _messageLiveData
    val choiceDialogLiveData: LiveData<String?>
        get() = _choiceDialogLiveData
    val wordsLiveData: LiveData<List<Word>>
        get() = _wordsLiveData
    val selectedWordsLiveData: LiveData<WordSelectionDto?>
        get() = _selectedWordsLiveData

    init {
        val categoryName = state.get<String>(Constants.ARG_CATEGORY_NAME)
        categoryName?.let {
            this.categoryName = categoryName
            subscribeWordsToData()
        }
    }

    fun onBtnStartClick() {
        if (selectedWordList.size < Constants.ANSWERS_COUNT) {
            showWarningMessage()
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

    private fun subscribeWordsToData() {
        viewModelScope.launch {
            _wordsLiveData.value = wordsInteractor.getWordsByCategory(categoryName)
        }
    }

    fun sendDTO(exerciseChoiceDto: ExerciseChoiceDto) {
        val dto = WordSelectionDto(
            exerciseChoiceDto.isTranslationDirection,
            selectedWordList,
            exerciseChoiceDto.exercise
        )
        _selectedWordsLiveData.value = dto
    }

    private fun showWarningMessage() {
        _messageLiveData.call()
    }
}