package com.pamn.playground.control

import com.pamn.playground.sharedKernel.AppStateStore
import com.pamn.playground.sharedKernel.HistoryStore

object UndoCommand : Command {
    override fun execute(state: AppStateStore, history: HistoryStore) {
        val current = state.snapshot()
        history.popUndo(current)?.let { prev ->
            state.set(prev)
        }
    }
}
