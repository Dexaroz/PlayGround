package com.pamn.playground.control

import com.pamn.playground.model.Media
import com.pamn.playground.sharedKernel.AppStateStore
import com.pamn.playground.sharedKernel.HistoryStore

class SetCapturePreviewCommand(private val bytes: ByteArray) : Command {
    override fun execute(state: AppStateStore, history: HistoryStore) {
        val before = state.snapshot()
        val after  = before.copy(media = Media.CameraPreview(bytes))
        if (after != before) { history.pushUndo(before); state.set(after) }
    }
}