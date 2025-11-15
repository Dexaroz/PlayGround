package com.pamn.playground.sharedKernel

import com.pamn.playground.model.AppState

class HistoryStore(private val maxSize: Int = 50) {
    private val undo = ArrayDeque<AppState>()
    private val redo = ArrayDeque<AppState>()

    fun pushUndo(prev: AppState) {
        undo.addLast(prev)
        if (undo.size > maxSize) undo.removeFirst()
        redo.clear()
    }

    fun canUndo() = undo.isNotEmpty()
    fun canRedo() = redo.isNotEmpty()

    fun popUndo(current: AppState): AppState? {
        if (!canUndo()) return null
        val prev = undo.removeLast()
        redo.addLast(current)
        return prev
    }

    fun popRedo(current: AppState): AppState? {
        if (!canRedo()) return null
        val next = redo.removeLast()
        undo.addLast(current)
        return next
    }

    fun clearAll() { undo.clear(); redo.clear() }
}