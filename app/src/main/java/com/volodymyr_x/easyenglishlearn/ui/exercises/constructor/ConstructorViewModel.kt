package com.volodymyr_x.easyenglishlearn.ui.exercises.constructor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volodymyr_x.easyenglishlearn.Constants
import com.volodymyr_x.easyenglishlearn.domain.exercises.CheckConstructorAnswerUseCase
import com.volodymyr_x.easyenglishlearn.ui.model.WordUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConstructorViewModel @Inject constructor(
    state: SavedStateHandle,
    private val checkConstructorAnswerUseCase: CheckConstructorAnswerUseCase
) : ViewModel() {
    val isLexemeToTranslationFlow: StateFlow<Boolean> =
        state.getStateFlow(Constants.IS_LEXEME_TO_TRANSLATION, true)
    val wordListFlow: StateFlow<List<WordUI>> =
        state.getStateFlow(Constants.SELECTED_WORDS, emptyList())
    private val _exerciseState = MutableStateFlow<ConstructorState>(ConstructorState.LoadingState)
    val exerciseState: StateFlow<ConstructorState> = _exerciseState.asStateFlow()

    init {
        viewModelScope.launch {
            isLexemeToTranslationFlow.combine(wordListFlow) { isLexemeToTranslation, wordList ->
                checkConstructorAnswerUseCase(
                    currentExerciseState = ConstructorState.LoadingState,
                    wordList = wordList,
                    isLexemeToTranslation = isLexemeToTranslation
                )
            }.collect { newState ->
                _exerciseState.value = newState
            }
        }
    }

    fun onEvent(event: ConstructorEvent) {
        when (event) {
            is ConstructorEvent.LetterButtonClicked -> answerCheck(event)
            is ConstructorEvent.UndoButtonClicked -> handleUndoLastLetter()
        }
    }

    private fun handleUndoLastLetter() {
        viewModelScope.launch {
            when (val state = _exerciseState.value) {
                is ConstructorState.StageState -> {
                    val updatedStageState = checkConstructorAnswerUseCase(
                        currentExerciseState = ConstructorState.UndoStageState(state.data.copy()),
                    )
                    _exerciseState.update { updatedStageState }
                }
                else -> Unit
            }
        }
    }

    fun answerCheck(event: ConstructorEvent.LetterButtonClicked) {
        viewModelScope.launch {
            when (val state = _exerciseState.value) {
                is ConstructorState.StageState -> {
                    val updatedStageState = checkConstructorAnswerUseCase(
                        userAnswer = event.letter,
                        currentExerciseState = state,
                    )
                    _exerciseState.update { updatedStageState }
                }
                else -> Unit
            }
        }
    }
}
