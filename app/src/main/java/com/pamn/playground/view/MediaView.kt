package com.pamn.playground.view

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pamn.playground.model.AppState
import com.pamn.playground.model.Media

@Composable
fun MediaView(state: AppState, commands: Map<String, () -> Unit>) {
    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Media", style = MaterialTheme.typography.titleMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FilledTonalButton(onClick = { commands["media:pick"]?.invoke() }) {
                Text("Choose picture")
            }
            OutlinedButton(onClick = { commands["media:capture"]?.invoke() }) {
                Text("Take picture")
            }
        }

        Divider()

        when (val m = state.media) {
            is Media.Gallery -> {
                AsyncImage(
                    model = m.coilModel(),
                    contentDescription = "Selected image",
                    modifier = Modifier.fillMaxWidth().heightIn(max = 420.dp)
                )
            }
            is Media.CameraPreview -> {
                val bytes = m.bytes
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                Image(
                    bitmap = bmp.asImageBitmap(),
                    contentDescription = "Camera preview",
                    modifier = Modifier.fillMaxWidth().heightIn(max = 420.dp)
                )
            }
            null -> Text("No image yet. Pick one or take a picture.")
        }
    }
}
