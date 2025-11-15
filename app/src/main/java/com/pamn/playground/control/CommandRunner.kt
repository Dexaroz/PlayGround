package com.pamn.playground.control

import com.pamn.playground.sharedKernel.AppStateStore
import com.pamn.playground.sharedKernel.HistoryStore


class CommandRunner(
    val state: AppStateStore,
    val history: HistoryStore
) {
    fun run(cmd: Command) = cmd.execute(state, history)

    fun run(key: String, registry: Map<String, Command>) {
        registry[key]?.execute(state, history)
    }
}