package com.pamn.playground.sharedKernel

import com.pamn.playground.model.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppStateStore(initial: AppState) {
    private val flow = MutableStateFlow(initial)
    fun snapshot(): AppState = flow.value
    fun set(new: AppState) { flow.value = new }
    fun state(): StateFlow<AppState> = flow
}