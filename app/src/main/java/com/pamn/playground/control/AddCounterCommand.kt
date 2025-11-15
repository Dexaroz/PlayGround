package com.pamn.playground.control

import com.pamn.playground.sharedKernel.AppStateStore
import com.pamn.playground.sharedKernel.HistoryStore

class AddCounterCommand(private val delta: Int) : Command {
    override fun execute(state: AppStateStore, history: HistoryStore) {
        val before = state.snapshot()
        val after = before.copy(counter = before.counter.add(delta))
        if (after != before) {
            history.pushUndo(before)
            state.set(after)
        }
    }
}