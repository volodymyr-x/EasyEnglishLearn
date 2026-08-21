package com.volodymyr_x.easyenglishlearn.ui.exercises.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volodymyr_x.easyenglishlearn.domain.exercises.CheckQuizAnswerUseCase
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

@HiltViewModel(assistedFactory = QuizViewModel.Factory::class)
class QuizViewModel @AssistedInject constructor(
    @Assisted wordSelectionResult: WordSelectionResult,
    private val checkQuizAnswerUseCase: CheckQuizAnswerUseCase
) : ViewModel() {
    private val _exerciseState = MutableStateFlow<QuizState>(QuizState.LoadingState)
    val exerciseState: StateFlow<QuizState> = _exerciseState.asStateFlow()

    init {
        viewModelScope.launch {
            _exerciseState.update { state ->
                checkQuizAnswerUseCase(
                    currentExerciseState = state,
                    wordList = wordSelectionResult.selectedWordList,
                    isLexemeToTranslation = wordSelectionResult.isLexemeToTranslation
                )
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(wordSelectionResult: WordSelectionResult): QuizViewModel
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
