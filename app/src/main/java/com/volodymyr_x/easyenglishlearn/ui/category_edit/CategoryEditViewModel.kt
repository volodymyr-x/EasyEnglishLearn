package com.volodymyr_x.easyenglishlearn.ui.category_edit

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volodymyr_x.easyenglishlearn.R
import com.volodymyr_x.easyenglishlearn.domain.WordsInteractor
import com.volodymyr_x.easyenglishlearn.model.Word
import com.volodymyr_x.easyenglishlearn.util.ResourceProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = CategoryEditViewModel.Factory::class)
class CategoryEditViewModel @AssistedInject constructor(
    @Assisted private val currentCategoryName: String,
    private val wordsInteractor: WordsInteractor,
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    private val _categoryEditState = MutableStateFlow(CategoryEditState())
    val categoryEditState: StateFlow<CategoryEditState>
        get() = _categoryEditState

    private val _categoryEditAction = Channel<CategoryEditAction>()
    val categoryEditAction: Flow<CategoryEditAction>
        get() = _categoryEditAction.receiveAsFlow()

    init {
        viewModelScope.launch {
            val wordsByCategory = wordsInteractor.getWordsByCategory(currentCategoryName)
            _categoryEditState.update { state ->
                state.copy(
                    categoryName = currentCategoryName,
                    oldCategoryName = currentCategoryName,
                    words = wordsByCategory
                )
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(currentCategoryName: String): CategoryEditViewModel
    }

    private fun onBtnSaveCategoryClick() {
        val state = _categoryEditState.value
        val newCategoryName = state.categoryName
        if (newCategoryName.isBlank()) {
            showMessage(R.string.cef_toast_save_edit_category)
        } else {
            if (state.oldCategoryName.isEmpty()) {
                addNewCategory(newCategoryName, state.words)
            } else {
                updateCategory(state.oldCategoryName, newCategoryName, state.words)
            }
            viewModelScope.launch {
                _categoryEditAction.send(CategoryEditAction.CloseScreen)
            }
        }
    }

    private fun onBtnSaveWordClick() {
        val state = _categoryEditState.value
        if (isTextFieldsNotEmpty) {
            val newWord = Word(
                lexeme = state.lexeme.trim(),
                translation = state.translation.trim()
            )
            val newList = mutableListOf<Word>().apply {
                addAll(state.words)
                if (state.wordIndex >= 0) {
                    this[state.wordIndex] = newWord
                } else {
                    add(newWord)
                }
            }
            cleanTextFields()
            _categoryEditState.update { state ->
                state.copy(words = newList)
            }
        } else {
            showMessage(R.string.cef_toast_save_word_empty_fields)
        }
    }

    private fun onIconRemoveWordClick(word: Word) {
        val wordsByCategory = _categoryEditState.value.words.toTypedArray()

        val updatedWords = listOf(*wordsByCategory) - word
        cleanTextFields()
        _categoryEditState.update { state ->
            state.copy(words = updatedWords)
        }
    }

    private fun onWordClick(word: Word) {
        _categoryEditState.update { state ->
            state.copy(
                lexeme = word.lexeme,
                translation = word.translation,
                wordIndex = state.words.indexOf(word)
            )
        }
    }

    private val isTextFieldsNotEmpty: Boolean
        get() {
            val state = _categoryEditState.value
            return state.categoryName.isNotBlank()
                    && state.lexeme.isNotBlank()
                    && state.translation.isNotBlank()
        }

    private fun cleanTextFields() {
        _categoryEditState.update { state ->
            state.copy(
                lexeme = "",
                translation = "",
                wordIndex = -1
            )
        }
    }

    private fun showMessage(@StringRes resId: Int) {
        viewModelScope.launch {
            _categoryEditAction.send(CategoryEditAction.ShowMessage(resourceProvider.getString(resId)))
        }
    }

    private fun addNewCategory(categoryName: String, wordList: List<Word>) {
        viewModelScope.launch {
            wordsInteractor.addNewCategory(wordList, categoryName)
        }
    }

    private fun updateCategory(
        oldCategoryName: String,
        newCategoryName: String,
        wordList: List<Word>
    ) {
        viewModelScope.launch {
            wordsInteractor.updateCategory(
                oldCategoryName,
                newCategoryName,
                wordList
            )
        }
    }

    private fun updateCategoryName(newValue: String) {
        _categoryEditState.update { state ->
            state.copy(
                categoryName = newValue
            )
        }
    }

    private fun updateLexeme(newValue: String) {
        _categoryEditState.update { state ->
            state.copy(
                lexeme = newValue
            )
        }
    }

    private fun updateTranslation(newValue: String) {
        _categoryEditState.update { state ->
            state.copy(
                translation = newValue
            )
        }
    }

    fun onEvent(event: CategoryEditEvent) {
        when (event) {
            CategoryEditEvent.SaveCategory -> onBtnSaveCategoryClick()
            CategoryEditEvent.AddWord -> onBtnSaveWordClick()
            is CategoryEditEvent.RemoveWord -> onIconRemoveWordClick(event.word)
            is CategoryEditEvent.CategoryNameUpdate -> updateCategoryName(event.newValue)
            is CategoryEditEvent.LexemeUpdate -> updateLexeme(event.newValue)
            is CategoryEditEvent.TranslationUpdate -> updateTranslation(event.newValue)
            CategoryEditEvent.CleanFields -> cleanTextFields()
            is CategoryEditEvent.OnWordClick -> onWordClick(event.word)
        }
    }
}
