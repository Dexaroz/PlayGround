package com.pamn.playground.control

import com.pamn.playground.model.AppState
import com.pamn.playground.model.Counter
import com.pamn.playground.sharedKernel.AppStateStore
import com.pamn.playground.sharedKernel.HistoryStore
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class ResetCounterCommandTest {
    @Test
    fun reset_sets_counter_to_zero_and_records_undo() {
        val store = AppStateStore(AppState(counter = Counter(9)))
        val history = HistoryStore()
        ResetCounterCommand.execute(store, history)
        assertEquals(Counter(0), store.snapshot().counter)
        assertTrue(history.canUndo())
    }

    @Test
    fun reset_on_zero_keeps_same_value_and_does_not_record_history() {
        val store = AppStateStore(AppState(counter = Counter(0)))
        val history = HistoryStore()
        ResetCounterCommand.execute(store, history)
        assertEquals(Counter(0), store.snapshot().counter)
        assertTrue(!history.canUndo() && !history.canRedo())
    }
}