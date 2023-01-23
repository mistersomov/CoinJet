package com.mistersomov.coinjet.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<State : Any, Action : Any, Event : Any>(initState: State) :
    ViewModel() {

    private val _viewStates = MutableStateFlow(initState)
    private val _viewActions =
        MutableSharedFlow<Action?>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    protected var viewState: State
        get() = _viewStates.value
        set(value) {
            _viewStates.value = value
        }

    protected var viewAction: Action?
        get() = _viewActions.replayCache.last()
        set(value) {
            _viewActions.tryEmit(value)
        }

    fun viewStates(): WrappedStateFlow<State> = WrappedStateFlow(_viewStates.asStateFlow())

    fun viewActions(): WrappedSharedFlow<Action?> = WrappedSharedFlow(_viewActions.asSharedFlow())

    abstract fun obtainEvent(event: Event)
}