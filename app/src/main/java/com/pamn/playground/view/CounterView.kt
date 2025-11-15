package com.pamn.playground.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Redo
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Redo
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Undo
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pamn.playground.view.AndroidPlaygroundFunctionsBar

@Composable
fun CounterView(
    value: Int,
    commands: Map<String, () -> Unit>,
    canUndo: Boolean,
    canRedo: Boolean
) {
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Counter", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))
        Text("$value", style = MaterialTheme.typography.displaySmall)

        Spacer(Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilledTonalButton(
                onClick = { commands["counter:inc+1"]?.invoke() },
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp)
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "+1")
                Spacer(Modifier.width(8.dp))
                Text("+1")
            }

            OutlinedButton(
                onClick = { commands["counter:inc-1"]?.invoke() },
                enabled = value > 0,
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp)
            ) {
                Icon(Icons.Rounded.Remove, contentDescription = "-1")
                Spacer(Modifier.width(8.dp))
                Text("-1")
            }

            TextButton(onClick = { commands["counter:reset"]?.invoke() }) {
                Icon(Icons.Rounded.Delete, contentDescription = "Reset")
                Spacer(Modifier.width(6.dp))
                Text("Reset")
            }
        }

        Spacer(Modifier.height(12.dp))
        Divider()
        Spacer(Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { commands["undo"]?.invoke() },
                enabled = canUndo
            ) {
                Icon(Icons.Rounded.Undo, contentDescription = "Undo")
                Spacer(Modifier.width(8.dp))
                Text("Undo")
            }
            OutlinedButton(
                onClick = { commands["redo"]?.invoke() },
                enabled = canRedo
            ) {
                Icon(Icons.Rounded.Redo, contentDescription = "Redo")
                Spacer(Modifier.width(8.dp))
                Text("Redo")
            }
        }

        Spacer(Modifier.height(24.dp))
        AndroidPlaygroundFunctionsBar(commands)
    }
}
