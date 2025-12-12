package com.app.core.extension

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class PatternViewModel<State, Intent, Effect>(
    initialState: State
) : ViewModel() {
    @Suppress("PropertyName")
    protected val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> get() = _state.asStateFlow()

    @Suppress("PropertyName")
    protected val _effect = Channel<Effect>()
    val effect: Channel<Effect> get() = _effect

    abstract fun onIntent(intent: Intent)

    protected fun reduce(update: State.() -> State) {
        _state.value = _state.value.update()
    }

    protected fun sendEffect(effect: Effect) {
        _effect.trySend(effect)
    }
}