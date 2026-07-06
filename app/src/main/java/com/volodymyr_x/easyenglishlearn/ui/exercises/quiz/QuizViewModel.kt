package com.volodymyr_x.easyenglishlearn.ui.exercises.quiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volodymyr_x.easyenglishlearn.Constants
import com.volodymyr_x.easyenglishlearn.domain.exercises.CheckQuizAnswerUseCase
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
class QuizViewModel @Inject constructor(
    state: SavedStateHandle,
    private val checkQuizAnswerUseCase: CheckQuizAnswerUseCase
) : ViewModel() {
    val isLexemeToTranslationFlow: StateFlow<Boolean> =
        state.getStateFlow(Constants.IS_LEXEME_TO_TRANSLATION, true)
    val wordListFlow: StateFlow<List<WordUI>> =
        state.getStateFlow(Constants.SELECTED_WORDS, emptyList())
    private val _exerciseState = MutableStateFlow<QuizState>(QuizState.LoadingState)
    val exerciseState: StateFlow<QuizState> = _exerciseState.asStateFlow()

    init {
        viewModelScope.launch {
            isLexemeToTranslationFlow.combine(wordListFlow) { isLexemeToTranslation, wordList ->
                checkQuizAnswerUseCase(
                    currentExerciseState = QuizState.LoadingState,
                    wordList = wordList,
                    isLexemeToTranslation = isLexemeToTranslation
                )
            }.collect { newState ->
                _exerciseState.value = newState
            }
        }
    }

    fun onAnswerChecked(answer: String) {
        viewModelScope.launch {
            when (val state = _exerciseState.value) {
                is QuizState.StageState -> {
                    val updatedStageState = checkQuizAnswerUseCase(
                        userAnswer = answer,
                        currentExerciseState = state,
                    )
                    _exerciseState.update { updatedStageState }
                }
                else -> Unit
            }
        }
    }
}
