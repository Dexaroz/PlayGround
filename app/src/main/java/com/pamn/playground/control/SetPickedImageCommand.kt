package com.pamn.playground.control

import android.net.Uri
import com.pamn.playground.model.Media
import com.pamn.playground.sharedKernel.AppStateStore
import com.pamn.playground.sharedKernel.HistoryStore

class SetPickedImageCommand(private val uri: Uri) : Command {
    override fun execute(state: AppStateStore, history: HistoryStore) {
        val before = state.snapshot()
        val after = before.copy(media = Media.Gallery(uri.toString()))
        if (after != before) {
            history.pushUndo(before); state.set(after)
        }
    }
}