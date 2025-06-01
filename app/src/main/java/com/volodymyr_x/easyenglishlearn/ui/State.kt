package com.volodymyr_x.easyenglishlearn.ui

sealed class State {
    object LoadingState : State()

    object IdleState : State()

    open class CompletedState<out T : Any>(val data: T) : State()

    open class CompletedStateNoData : CompletedState<Any>(Any())

    open class DataState<out T : Any>(val data: T) : State()

    data class ErrorState(val message: String? = null, val exception: Throwable? = null) : State()
}
