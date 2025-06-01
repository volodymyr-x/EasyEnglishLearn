package com.volodymyr_x.easyenglishlearn.ui.category_edit

import android.text.TextUtils
import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volodymyr_x.easyenglishlearn.Constants
import com.volodymyr_x.easyenglishlearn.R
import com.volodymyr_x.easyenglishlearn.domain.WordsInteractor
import com.volodymyr_x.easyenglishlearn.model.Word
import com.volodymyr_x.easyenglishlearn.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CategoryEditViewModel @Inject constructor(
    state: SavedStateHandle,
    private val wordsInteractor: WordsInteractor,
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    private var oldCategoryName: String = ""
    private var wordIndex = 0
    private var categoryName: String = ""
    private var lexeme: String = ""
    private var translation: String = ""
    private val _categoryEditState =
        MutableStateFlow<CategoryEditState>(CategoryEditState.IdleState)
    val categoryEditState: StateFlow<CategoryEditState>
        get() = _categoryEditState

    private val _words =
        MutableStateFlow<List<Word>>(emptyList())
    val words: StateFlow<List<Word>>
        get() = _words

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
                addNewCategory(newCategoryName, _words.value)
            } else {
                updateCategory(oldCategoryName, newCategoryName, _words.value)
            }
            changeCategoryState(CategoryEditState.CloseScreenState)
        }
    }

    fun onBtnSaveWordClick(categoryName: String, lexeme: String, translation: String) {
        this.categoryName = categoryName
        this.lexeme = lexeme
        this.translation = translation
        if (isTextFieldsNotEmpty) {
            val newWord = Word(lexeme.trim(), translation.trim())
            val newList = mutableListOf<Word>().apply {
                addAll(_words.value)
            }
            if (wordIndex >= 0) {
                newList[wordIndex] = newWord
            } else {
                newList.add(newWord)
            }
            cleanTextFields()
            viewModelScope.launch {
                _words.emit(newList)
            }
        } else {
            showMessage(R.string.cef_toast_save_word_empty_fields)
        }
    }

    fun onBtnCleanClick() {
        cleanTextFields()
    }

    fun onIconRemoveWordClick(word: Word) {
        val newList = mutableListOf<Word>().apply {
            addAll(_words.value)
        }
        newList.remove(word)
        cleanTextFields()
        viewModelScope.launch {
            _words.emit(newList)
        }
    }

    fun onItemClick(word: Word) {
        lexeme = word.lexeme
        translation = word.translation
        wordIndex = _words.value.indexOf(word)
        updateCurrentWord()
    }

    private val isTextFieldsNotEmpty: Boolean
        get() = !TextUtils.isEmpty(categoryName.trim()) &&
                !TextUtils.isEmpty(lexeme.trim()) &&
                !TextUtils.isEmpty(translation.trim())

    private fun subscribeWordsToData() {
        viewModelScope.launch {
            _words.emit(wordsInteractor.getWordsByCategory(oldCategoryName))
        }
    }

    private fun cleanTextFields() {
        lexeme = Constants.EMPTY_STRING
        translation = Constants.EMPTY_STRING
        wordIndex = -1
        updateCurrentWord()
    }

    private fun showMessage(@StringRes resId: Int) {
        changeCategoryState(CategoryEditState.ShowMessage(resourceProvider.getString(resId)))
    }

    private fun addNewCategory(categoryName: String, wordList: List<Word>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                wordsInteractor.addNewCategory(wordList, categoryName)
            }
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
        }
    }

    private fun updateCurrentWord() {
        changeCategoryState(CategoryEditState.CurrentWord(Pair(lexeme, translation)))
    }

    private fun changeCategoryState(state: CategoryEditState) {
        _categoryEditState.value = state
        _categoryEditState.value = CategoryEditState.IdleState
    }
}
