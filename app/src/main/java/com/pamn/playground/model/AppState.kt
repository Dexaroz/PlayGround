package com.pamn.playground.model

data class AppState (
    val counter: Counter = Counter.ZERO,
    val media: Media? = null
)
