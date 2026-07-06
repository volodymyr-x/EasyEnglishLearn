package com.volodymyr_x.easyenglishlearn.ui.exercises.quiz

sealed class QuizState {
    data object LoadingState : QuizState()

    data class CompletedState(val data: QuizStageState) : QuizState()

    data class StageState(val data: QuizStageState) : QuizState()
}
