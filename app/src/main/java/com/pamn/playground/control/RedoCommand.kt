package com.pamn.playground.control

import com.pamn.playground.sharedKernel.AppStateStore
import com.pamn.playground.sharedKernel.HistoryStore

object RedoCommand : Command {
    override fun execute(state: AppStateStore, history: HistoryStore) {
        val current = state.snapshot()
        history.popRedo(current)?.let { next ->
            state.set(next)
        }
    }
}