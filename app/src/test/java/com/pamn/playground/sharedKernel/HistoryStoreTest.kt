package com.pamn.playground.sharedKernel

import com.pamn.playground.model.AppState
import com.pamn.playground.model.Counter
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class HistoryStoreTest {
    @Test
    fun push_undo_and_pop_works() {
        val h = HistoryStore()
        val s0 = AppState(counter = Counter(0))
        val s1 = AppState(counter = Counter(1))

        h.pushUndo(s0)
        assertTrue(h.canUndo())

        val back = h.popUndo(current = s1)
        assertEquals(s0, back)
        assertTrue(h.canRedo())
    }

    @Test
    fun redo_gives_next_state_and_moves_current_to_undo() {
        val h = HistoryStore()
        val s0 = AppState(counter = Counter(0))
        val s1 = AppState(counter = Counter(1))
        val s2 = AppState(counter = Counter(2))

        h.pushUndo(s0)
        h.pushUndo(s1)

        val prev = h.popUndo(current = s2)
        assertEquals(s1, prev)
        assertTrue(h.canRedo())

        val next = h.popRedo(current = s1)
        assertEquals(s2, next)
        assertTrue(h.canUndo())
    }

    @Test
    fun new_pushUndo_clears_redo_stack() {
        val h = HistoryStore()
        val s0 = AppState(counter = Counter(0))
        val s1 = AppState(counter = Counter(1))
        val s2 = AppState(counter = Counter(2))

        h.pushUndo(s0)
        h.pushUndo(s1)

        val back = h.popUndo(current = s2)
        assertEquals(s1, back)
        assertTrue(h.canRedo())

        h.pushUndo(s1)
        assertFalse(h.canRedo())
    }

    @Test
    fun maxSize_cap_drops_oldest_undos() {
        val h = HistoryStore(maxSize = 2)
        val s0 = AppState(counter = Counter(0))
        val s1 = AppState(counter = Counter(1))
        val s2 = AppState(counter = Counter(2))
        val s3 = AppState(counter = Counter(3))

        h.pushUndo(s0)
        h.pushUndo(s1)
        h.pushUndo(s2)

        val back1 = h.popUndo(current = s3)
        assertEquals(s2, back1)

        val back2 = h.popUndo(current = s2)
        assertEquals(s1, back2)

        assertFalse(h.canUndo())
    }

    @Test
    fun pop_when_stacks_are_empty_returns_null_and_flags_false() {
        val h = HistoryStore()
        val current = AppState(counter = Counter(5))

        assertNull(h.popUndo(current))
        assertNull(h.popRedo(current))
        assertFalse(h.canUndo())
        assertFalse(h.canRedo())
    }

    @Test
    fun clearAll_empties_both_stacks() {
        val h = HistoryStore()
        val s0 = AppState(counter = Counter(0))
        val s1 = AppState(counter = Counter(1))

        h.pushUndo(s0)
        h.popUndo(current = s1)
        assertTrue(h.canUndo() || h.canRedo())

        h.clearAll()
        assertFalse(h.canUndo())
        assertFalse(h.canRedo())
    }
}