package com.volodymyr_x.easyenglishlearn.ui.exercises

import com.volodymyr_x.easyenglishlearn.ui.exercises.quiz.QuizStageState

sealed class ExerciseState {
    data object LoadingState : ExerciseState()

    data class CompletedState(val data: QuizStageState) : ExerciseState()

    data class StageState(val data: QuizStageState) : ExerciseState()
}
