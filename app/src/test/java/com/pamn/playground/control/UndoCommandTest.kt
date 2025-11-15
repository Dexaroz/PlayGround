package com.pamn.playground.control

import com.pamn.playground.model.AppState
import com.pamn.playground.model.Counter
import com.pamn.playground.sharedKernel.AppStateStore
import com.pamn.playground.sharedKernel.HistoryStore
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class UndoCommandTest {

    @Test
    fun undo_restores_previous_state_and_enables_redo() {
        val store = AppStateStore(AppState(counter = Counter(0)))
        val history = HistoryStore()
        AddCounterCommand(1).execute(store, history)
        AddCounterCommand(2).execute(store, history)
        UndoCommand.execute(store, history)
        assertEquals(Counter(1), store.snapshot().counter)
        assertTrue(history.canRedo())
    }

    @Test
    fun undo_when_history_empty_does_nothing() {
        val store = AppStateStore(AppState(counter = Counter(2)))
        val history = HistoryStore()
        UndoCommand.execute(store, history)
        assertEquals(Counter(2), store.snapshot().counter)
        assertFalse(history.canUndo())
        assertFalse(history.canRedo())
    }
}
