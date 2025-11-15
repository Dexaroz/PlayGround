package com.pamn.playground.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OtherView(commands: Map<String, () -> Unit>) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Otra vista", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Text("Aquí podrás montar tu próxima demo o pantalla.")
        Spacer(Modifier.height(16.dp))
        OutlinedButton(onClick = { commands["android:openUrl"]?.invoke() }) {
            Text("Abrir kotlinlang.org")
        }
    }
}