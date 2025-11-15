package com.pamn.playground.control

import com.pamn.playground.sharedKernel.AppStateStore
import com.pamn.playground.sharedKernel.HistoryStore

interface Command {
    fun execute(state: AppStateStore, history: HistoryStore)
}