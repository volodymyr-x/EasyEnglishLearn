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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class WordSelectionViewModel @Inject constructor(
    state: SavedStateHandle,
    private val wordsInteractor: WordsInteractor
) : ViewModel() {
    private var wordsByCategory: List<WordUI> = listOf()
    var categoryName = ""

    private val _wordSelectionState =
        MutableStateFlow<WordSelectionState>(WordSelectionState.IdleState)
    val wordSelectionState: StateFlow<WordSelectionState>
        get() = _wordSelectionState

    init {
        val categoryName = state.get<String>(Constants.ARG_CATEGORY_NAME)
        categoryName?.let {
            this.categoryName = categoryName
            loadWords()
        }
    }

    fun onBtnStartClick() {
        if (getSelectedWords().size < Constants.MIN_CHECKED_WORD_QUANTITY) {
            changeState(WordSelectionState.ShowMessage)
        } else {
            changeState(WordSelectionState.OpenDialog(categoryName))
        }
    }

    private fun getSelectedWords() = wordsByCategory.filter { it.isChecked }

    fun onChooseAllClick(checked: Boolean) {
        wordsByCategory.onEach { it.isChecked = checked }
        changeState(WordSelectionState.UpdateWords(wordsByCategory, checked))
    }

    fun onItemCheckBoxChange(checked: Boolean, wordId: Long) {
        wordsByCategory.firstOrNull { it.id == wordId }?.let {
            it.isChecked = checked
        }
        val isChooseAllChecked = getSelectedWords().size == wordsByCategory.size
        changeState(WordSelectionState.UpdateWords(wordsByCategory, isChooseAllChecked))
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
            changeState(WordSelectionState.UpdateWords(words))
        }
    }

    fun sendDTO(exerciseChoiceDto: ExerciseChoiceDto?) {
        exerciseChoiceDto?.let {
            val dto = WordSelectionDto(
                exerciseChoiceDto.isTranslationDirection,
                getSelectedWords() as ArrayList<WordUI>,
                exerciseChoiceDto.exercise
            )
            changeState(WordSelectionState.StartExercise(dto))
        }
    }

    private fun changeState(state: WordSelectionState) {
        _wordSelectionState.value = state
        _wordSelectionState.value = WordSelectionState.IdleState
    }
}
