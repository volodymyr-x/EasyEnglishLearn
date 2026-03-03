package com.volodymyr_x.easyenglishlearn.ui.word_selection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volodymyr_x.easyenglishlearn.Constants
import com.volodymyr_x.easyenglishlearn.domain.WordsInteractor
import com.volodymyr_x.easyenglishlearn.ui.model.WordUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WordSelectionViewModel @Inject constructor(
    state: SavedStateHandle,
    private val wordsInteractor: WordsInteractor
) : ViewModel() {
    private var wordsByCategory: List<WordUI> = listOf()
    var categoryName = ""

    private val _wordSelectionStateOld =
        MutableStateFlow<WordSelectionStateOld>(WordSelectionStateOld.IdleStateOld)
    val wordSelectionStateOld: StateFlow<WordSelectionStateOld>
        get() = _wordSelectionStateOld

    private val _wordSelectionState = MutableStateFlow(WordSelectionState())
    val wordSelectionState = _wordSelectionState.asStateFlow()


    init {
        val categoryName = state.get<String>(Constants.ARG_CATEGORY_NAME)
        categoryName?.let {
            this.categoryName = categoryName
            _wordSelectionState.update { it.copy(categoryName = categoryName) }
            loadWords()
        }
    }

    fun onBtnStartClick() {
        if (getSelectedWords().size < Constants.MIN_CHECKED_WORD_QUANTITY) {
            changeState(WordSelectionStateOld.ShowMessage)
        } else {
            changeState(WordSelectionStateOld.OpenDialog(categoryName))
        }
    }

    private fun getSelectedWords() = wordsByCategory.filter { it.isChecked }

    fun onChooseAllClick() {
        val checked = !_wordSelectionState.value.isChooseAllChecked
        wordsByCategory = wordsByCategory.map { it.copy(isChecked = checked) }
        changeState(WordSelectionStateOld.UpdateWords(wordsByCategory, checked))
        _wordSelectionState.update { it.copy(
            categoryWords = wordsByCategory,
            isChooseAllChecked = checked
        ) }
    }

    fun onItemCheckBoxChange(checkedWord: WordUI) {
        wordsByCategory = wordsByCategory.map {
            if (it.id == checkedWord.id) it.copy(isChecked = !it.isChecked)
            else it
        }
        val isChooseAllChecked = getSelectedWords().size == wordsByCategory.size
        changeState(WordSelectionStateOld.UpdateWords(wordsByCategory, isChooseAllChecked))
        _wordSelectionState.update { it.copy(
            categoryWords = wordsByCategory,
            isChooseAllChecked = isChooseAllChecked
        ) }
    }

    private fun loadWords() {
        viewModelScope.launch {
            val words = withContext(Dispatchers.IO) {
                wordsInteractor.getWordsByCategory(categoryName).map { word ->
                    WordUI(
                        word.id,
                        word.lexeme,
                        word.translation
                    )
                }
            }
            wordsByCategory = words
            changeState(WordSelectionStateOld.UpdateWords(words))
            _wordSelectionState.update { it.copy(categoryWords = wordsByCategory) }
        }
    }

    fun sendDTO(exerciseChoiceDto: ExerciseChoiceDto?) {
        exerciseChoiceDto?.let {
            val dto = WordSelectionDto(
                exerciseChoiceDto.isTranslationDirection,
                getSelectedWords() as ArrayList<WordUI>,
                exerciseChoiceDto.exercise
            )
            changeState(WordSelectionStateOld.StartExercise(dto))
        }
    }

    private fun changeState(state: WordSelectionStateOld) {
        _wordSelectionStateOld.value = state
        _wordSelectionStateOld.value = WordSelectionStateOld.IdleStateOld
    }

    fun onAction(action: WordSelectionAction) {
        when (action) {
            is WordSelectionAction.OnBtnStartClick -> onBtnStartClick()
            is WordSelectionAction.OnItemCheckBoxChange -> onItemCheckBoxChange(action.word)
            is WordSelectionAction.OnChooseAllClick -> onChooseAllClick()
        }
    }
}
