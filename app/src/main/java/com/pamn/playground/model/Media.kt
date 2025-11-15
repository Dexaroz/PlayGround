package com.pamn.playground.model


sealed interface Media {
    fun coilModel(): Any

    data class Gallery(val uri: String) : Media {
        override fun coilModel(): Any = uri
    }

    data class CameraPreview(val bytes: ByteArray) : Media {
        override fun coilModel(): Any = bytes
    }
}