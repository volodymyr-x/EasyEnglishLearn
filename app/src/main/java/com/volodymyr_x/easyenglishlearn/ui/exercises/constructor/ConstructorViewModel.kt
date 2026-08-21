package com.volodymyr_x.easyenglishlearn.ui.exercises.constructor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volodymyr_x.easyenglishlearn.domain.exercises.CheckConstructorAnswerUseCase
import com.volodymyr_x.easyenglishlearn.ui.word_selection.WordSelectionResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ConstructorViewModel.Factory::class)
class ConstructorViewModel @AssistedInject constructor(
    @Assisted wordSelectionResult: WordSelectionResult,
    private val checkConstructorAnswerUseCase: CheckConstructorAnswerUseCase
) : ViewModel() {
    private val _exerciseState = MutableStateFlow<ConstructorState>(ConstructorState.LoadingState)
    val exerciseState: StateFlow<ConstructorState> = _exerciseState.asStateFlow()

    init {
        viewModelScope.launch {
            _exerciseState.update { state ->
                checkConstructorAnswerUseCase(
                    currentExerciseState = state,
                    wordList = wordSelectionResult.selectedWordList,
                    isLexemeToTranslation = wordSelectionResult.isLexemeToTranslation
                )
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(wordSelectionResult: WordSelectionResult): ConstructorViewModel
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
