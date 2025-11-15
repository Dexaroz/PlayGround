package com.pamn.playground.model

@JvmInline
value class Counter(val value: Int) {
    fun add(delta: Int) : Counter {
        val next = value + delta;
        require(value >= 0) { "Counter can not be negative." }

        return Counter(next)
    }

    fun reset() : Counter = ZERO


    companion object { val ZERO = Counter(0) }
}