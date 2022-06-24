package com.vladimir_x.easyenglishlearn.ui.category_edit

import android.text.TextUtils
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.R
import com.vladimir_x.easyenglishlearn.domain.WordsInteractor
import com.vladimir_x.easyenglishlearn.model.Word
import com.vladimir_x.easyenglishlearn.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CategoryEditViewModel @Inject constructor(
    state: SavedStateHandle,
    private val wordsInteractor: WordsInteractor
) : ViewModel() {
    private val _messageLiveData: SingleLiveEvent<Int> = SingleLiveEvent()
    private val _fragmentCloseLiveData: SingleLiveEvent<Unit> = SingleLiveEvent()
    private val _wordsLiveData: MutableLiveData<List<Word>> = MutableLiveData<List<Word>>()
    private val _currentWordLiveData: MutableLiveData<Pair<String, String>> =
        MutableLiveData<Pair<String, String>>()
    private var oldCategoryName: String = ""
    private var wordIndex = 0
    private var categoryName: String = ""
    private var lexeme: String = ""
    private var translation: String = ""

    val wordsLiveData: LiveData<List<Word>>
        get() = _wordsLiveData

    val currentWordLiveData: LiveData<Pair<String, String>>
        get() = _currentWordLiveData

    val messageLiveData: LiveData<Int?>
        get() = _messageLiveData

    val fragmentCloseLiveData: LiveData<Unit?>
        get() = _fragmentCloseLiveData

    init {
        val categoryName = state.get<String>(Constants.ARG_CATEGORY_NAME)
        categoryName?.let {
            this.categoryName = categoryName
            this.oldCategoryName = categoryName
            cleanTextFields()
            subscribeWordsToData()
        }
    }

    fun onBtnSaveCategoryClick(categoryName: String) {
        val newCategoryName: String = categoryName.trim()
        if (TextUtils.isEmpty(newCategoryName)) {
            showMessage(R.string.cef_toast_save_edit_category)
        } else {
            if (TextUtils.isEmpty(oldCategoryName)) {
                _wordsLiveData.value?.let { addNewCategory(newCategoryName, it) }
            } else {
                _wordsLiveData.value?.let { updateCategory(oldCategoryName, newCategoryName, it) }
            }
            _fragmentCloseLiveData.call()
        }
    }

    fun onBtnSaveWordClick(categoryName: String, lexeme: String, translation: String) {
        this.categoryName = categoryName
        this.lexeme = lexeme
        this.translation = translation
        if (isTextFieldsNotEmpty) {
            val newWord = Word(lexeme.trim(), translation.trim())
            _wordsLiveData.value?.let { list ->
                val newList = mutableListOf<Word>().apply {
                    addAll(list)
                }
                if (wordIndex >= 0) {
                    newList[wordIndex] = newWord
                } else {
                    newList.add(newWord)
                }
                _wordsLiveData.value = newList
                cleanTextFields()
            }
        } else {
            showMessage(R.string.cef_toast_save_word_empty_fields)
        }
    }

    fun onBtnCleanClick() {
        cleanTextFields()
    }

    fun onIconRemoveWordClick(word: Word) {
        _wordsLiveData.value?.let { list ->
            val newList = mutableListOf<Word>().apply {
                addAll(list)
            }
            newList.remove(word)
            _wordsLiveData.value = newList
            cleanTextFields()
        }
    }

    fun onItemClick(word: Word) {
        lexeme = word.lexeme
        translation = word.translation
        wordIndex = _wordsLiveData.value?.indexOf(word) ?: -1
        updateCurrentWord()
    }

    private val isTextFieldsNotEmpty: Boolean
        get() = !TextUtils.isEmpty(categoryName.trim()) &&
                !TextUtils.isEmpty(lexeme.trim()) &&
                !TextUtils.isEmpty(translation.trim())

    private fun subscribeWordsToData() {
        viewModelScope.launch {
            _wordsLiveData.value = wordsInteractor.getWordsByCategory(oldCategoryName)
        }
    }

    private fun cleanTextFields() {
        lexeme = Constants.EMPTY_STRING
        translation = Constants.EMPTY_STRING
        wordIndex = -1
        updateCurrentWord()
    }

    private fun showMessage(@StringRes resId: Int) {
        _messageLiveData.value = resId
    }

    private fun addNewCategory(categoryName: String, wordList: List<Word>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                wordsInteractor.addNewCategory(wordList, categoryName)
            }
            showMessage(R.string.category_added)
        }
    }

    private fun updateCategory(
        oldCategoryName: String,
        newCategoryName: String,
        wordList: List<Word>
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                wordsInteractor.updateCategory(
                    oldCategoryName,
                    newCategoryName,
                    wordList
                )
            }
            showMessage(R.string.category_edited)
        }
    }

    private fun updateCurrentWord() {
        _currentWordLiveData.value = Pair(lexeme, translation)
    }
}