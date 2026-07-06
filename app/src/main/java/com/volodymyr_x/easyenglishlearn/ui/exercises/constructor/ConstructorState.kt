package com.volodymyr_x.easyenglishlearn.ui.exercises.constructor

sealed class ConstructorState {
    data object LoadingState : ConstructorState()

    data class CompletedState(val data: ConstructorStageState) : ConstructorState()

    data class StageState(val data: ConstructorStageState) : ConstructorState()

    data class UndoStageState(val data: ConstructorStageState) : ConstructorState()
}
