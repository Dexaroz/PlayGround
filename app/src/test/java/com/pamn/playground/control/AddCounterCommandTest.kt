package com.pamn.playground.control

import com.pamn.playground.model.AppState
import com.pamn.playground.model.Counter
import com.pamn.playground.sharedKernel.AppStateStore
import com.pamn.playground.sharedKernel.HistoryStore
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertThrows
import org.junit.Test

class AddCounterCommandTest {
    @Test
    fun inc_positive_increases_value_and_records_undo() {
        val store = AppStateStore(AppState(counter = Counter(5)))
        val history = HistoryStore()
        AddCounterCommand(3).execute(store, history)
        assertEquals(Counter(8), store.snapshot().counter)
        assertTrue(history.canUndo())
    }

    @Test
    fun inc_zero_keeps_same_value_and_does_not_record_history() {
        val store = AppStateStore(AppState(counter = Counter(5)))
        val history = HistoryStore()
        AddCounterCommand(0).execute(store, history)
        assertEquals(Counter(5), store.snapshot().counter)
        assertTrue(!history.canUndo() && !history.canRedo())
    }

    @Test
    fun inc_negative_down_to_zero_is_allowed_and_records_undo() {
        val store = AppStateStore(AppState(counter = Counter(2)))
        val history = HistoryStore()
        AddCounterCommand(-2).execute(store, history)
        assertEquals(Counter(0), store.snapshot().counter)
        assertTrue(history.canUndo())
    }

    @Test
    fun inc_negative_below_zero_throws_and_leaves_state_untouched() {
        val store = AppStateStore(AppState(counter = Counter(1)))
        val history = HistoryStore()
        assertThrows(IllegalArgumentException::class.java) {
            AddCounterCommand(-5).execute(store, history)
        }
        assertEquals(Counter(1), store.snapshot().counter)
        assertTrue(!history.canUndo() && !history.canRedo())
    }
}