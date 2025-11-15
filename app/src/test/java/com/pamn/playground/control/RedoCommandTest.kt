package com.pamn.playground.control

import com.pamn.playground.model.AppState
import com.pamn.playground.model.Counter
import com.pamn.playground.sharedKernel.AppStateStore
import com.pamn.playground.sharedKernel.HistoryStore
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class RedoCommandTest {
    @Test
    fun redo_restores_next_state_after_undo() {
        val store = AppStateStore(AppState(counter = Counter(0)))
        val history = HistoryStore()
        AddCounterCommand(1).execute(store, history)
        AddCounterCommand(2).execute(store, history)
        UndoCommand.execute(store, history)
        RedoCommand.execute(store, history)
        assertEquals(Counter(3), store.snapshot().counter)
        assertTrue(history.canUndo())
    }

    @Test
    fun new_command_after_undo_clears_redo_stack() {
        val store = AppStateStore(AppState(counter = Counter(0)))
        val history = HistoryStore()
        AddCounterCommand(5).execute(store, history)
        AddCounterCommand(1).execute(store, history)
        UndoCommand.execute(store, history)
        ResetCounterCommand.execute(store, history)
        RedoCommand.execute(store, history)
        assertEquals(Counter(0), store.snapshot().counter)
        assertFalse(history.canRedo())
    }

    @Test
    fun redo_when_history_empty_does_nothing() {
        val store = AppStateStore(AppState(counter = Counter(2)))
        val history = HistoryStore()
        RedoCommand.execute(store, history)
        assertEquals(Counter(2), store.snapshot().counter)
        assertFalse(history.canUndo())
        assertFalse(history.canRedo())
    }
}