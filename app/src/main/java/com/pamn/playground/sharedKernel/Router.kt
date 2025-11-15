package com.pamn.playground.sharedKernel

sealed class Router(val route: String, val label: String) {
    data object Counter : Router("counter", "Counter")
    data object Other   : Router("other",   "Other")
}