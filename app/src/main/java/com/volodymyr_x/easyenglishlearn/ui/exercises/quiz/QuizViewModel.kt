package com.volodymyr_x.easyenglishlearn.ui.exercises.quiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volodymyr_x.easyenglishlearn.Constants
import com.volodymyr_x.easyenglishlearn.domain.exercises.CheckQuizAnswerUseCase
import com.volodymyr_x.easyenglishlearn.ui.exercises.ExerciseState
import com.volodymyr_x.easyenglishlearn.ui.model.WordUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    state: SavedStateHandle,
    private val checkQuizAnswerUseCase: CheckQuizAnswerUseCase
) : ViewModel() {
    private val _exerciseState = MutableStateFlow<ExerciseState>(ExerciseState.LoadingState)
    val exerciseState: StateFlow<ExerciseState> = _exerciseState.asStateFlow()

    init {
        val isLexemeToTranslation =
            state.get<Boolean>(Constants.TRANSLATION_DIRECTION) ?: true
        val wordList: List<WordUI> =
            state.get<ArrayList<WordUI>>(Constants.SELECTED_WORDS) as? List<WordUI>
                ?: emptyList()
        viewModelScope.launch {
            _exerciseState.update { state ->
                checkQuizAnswerUseCase(
                    currentExerciseState = state,
                    wordList = wordList,
                    isLexemeToTranslation = isLexemeToTranslation
                )
            }
        }
    }

    fun onAnswerChecked(answer: String) {
        viewModelScope.launch {
            when (val state = _exerciseState.value) {
                is ExerciseState.StageState -> {
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
