package com.pamn.playground.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AndroidPlaygroundFunctionsBar (
    commands: Map<String, () -> Unit>
) {
    Text("Android demos", style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(8.dp))

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FilledTonalButton(onClick = { commands["android:share"]?.invoke() }) {
                Text("Share text")
            }
            OutlinedButton(onClick = { commands["android:copyCounter"]?.invoke() }) {
                Text("Copy counter")
            }
        }
    }
}