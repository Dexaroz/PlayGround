package com.pamn.playground.modelo

import com.pamn.playground.model.Counter
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotSame
import org.junit.Assert.assertThrows
import org.junit.Test

class CounterTest {
    @Test
    fun add_positive_increases_value() {
        val c1 = Counter(5)
        val c2 = c1.add(3)
        assertEquals(Counter(8), c2)
        assertEquals(Counter(5), c1)
        assertNotSame(c1, c2)
    }

    @Test
    fun add_zero_keeps_same_value() {
        val c1 = Counter(5)
        val c2 = c1.add(0)
        assertEquals(Counter(5), c2)
    }

    @Test
    fun add_negative_down_to_zero_is_allowed() {
        val c1 = Counter(2)
        val c2 = c1.add(-2)
        assertEquals(Counter(0), c2)
    }

    @Test
    fun add_negative_below_zero_throws() {
        val c1 = Counter(1)
        assertThrows(IllegalArgumentException::class.java) {
            c1.add(-5)
        }
    }

    @Test
    fun reset_returns_zero_without_mutating_original() {
        val c1 = Counter(9)
        val c2 = c1.reset()
        assertEquals(Counter(0), c2)
        assertEquals(Counter(9), c1)
    }

    @Test
    fun value_equality_works_for_value_class() {
        val a = Counter(3)
        val b = Counter(3)
        assertEquals(a, b)
    }
}