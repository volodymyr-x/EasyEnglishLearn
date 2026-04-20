package com.volodymyr_x.easyenglishlearn.ui.word_selection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volodymyr_x.easyenglishlearn.Constants
import com.volodymyr_x.easyenglishlearn.domain.WordsInteractor
import com.volodymyr_x.easyenglishlearn.ui.model.WordUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WordSelectionViewModel @Inject constructor(
    state: SavedStateHandle,
    private val wordsInteractor: WordsInteractor
) : ViewModel() {
    private val _screenState = MutableStateFlow(WordSelectionState())
    val screenState = _screenState.asStateFlow()

    private val _wordSelectionAction = Channel<WordSelectionAction>()
    val wordSelectionAction: Flow<WordSelectionAction>
        get() = _wordSelectionAction.receiveAsFlow()


    init {
        val categoryName = state.get<String>(Constants.ARG_CATEGORY_NAME)
        categoryName?.let {
            updateScreenState { it.copy(categoryName = categoryName) }
            loadWords()
        }
    }

    fun onAction(action: WordSelectionEvent) {
        when (action) {
            is WordSelectionEvent.OnBtnStartClick -> onBtnStartClick()
            is WordSelectionEvent.OnItemCheckBoxChange -> onItemCheckBoxChange(action.word)
            is WordSelectionEvent.OnChooseAllClick -> onChooseAllClick()
            is WordSelectionEvent.SetExerciseChoiceDto -> sendDTO(action.exerciseChoiceDto)
            WordSelectionEvent.HideDialog -> hideChooseExerciseDialog()
        }
    }

    private fun onBtnStartClick() {
        if (getSelectedWords().size < Constants.MIN_CHECKED_WORD_QUANTITY) {
            viewModelScope.launch {
                _wordSelectionAction.send(WordSelectionAction.ShowMessage)
            }
        } else {
            updateScreenState { it.copy(openChooseExerciseDialog = true) }
        }
    }

    private fun getSelectedWords() = _screenState.value.categoryWords.filter { it.isChecked }

    private fun onChooseAllClick() {
        updateScreenState { state ->
            val checked = !state.isChooseAllChecked
            state.copy(
                categoryWords = state.categoryWords.map { it.copy(isChecked = checked) },
                isChooseAllChecked = checked
            )
        }
    }

    private fun onItemCheckBoxChange(checkedWord: WordUI) {
        updateScreenState { state ->
            val updatedWords = state.categoryWords.map {
                if (it.id == checkedWord.id) it.copy(isChecked = !it.isChecked)
                else it
            }
            val areAllWordsChecked = updatedWords.all { it.isChecked }
            state.copy(
                categoryWords = updatedWords,
                isChooseAllChecked = areAllWordsChecked
            )
        }
    }

    private fun loadWords() {
        viewModelScope.launch {
            val categoryName = _screenState.value.categoryName
            val words = withContext(Dispatchers.IO) {
                wordsInteractor.getWordsByCategory(categoryName).map { word ->
                    WordUI(
                        word.id,
                        word.lexeme,
                        word.translation
                    )
                }
            }
            updateScreenState { it.copy(categoryWords = words) }
        }
    }

    private fun sendDTO(exerciseChoiceDto: ExerciseChoiceDto?) {
        hideChooseExerciseDialog()
        exerciseChoiceDto?.let {
            val dto = WordSelectionDto(
                exerciseChoiceDto.isWordToTranslation,
                getSelectedWords() as ArrayList<WordUI>,
                exerciseChoiceDto.exerciseType
            )
            viewModelScope.launch {
                _wordSelectionAction.send(WordSelectionAction.StartExercise(dto))
            }
        }
    }

    private fun hideChooseExerciseDialog() {
        updateScreenState { it.copy(openChooseExerciseDialog = false) }
    }

    private fun updateScreenState(
        function: (WordSelectionState) -> WordSelectionState
    ) {
        _screenState.update(function)
    }
}
