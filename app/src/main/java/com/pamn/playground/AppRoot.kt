package com.pamn.playground

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pamn.playground.control.AddCounterCommand
import com.pamn.playground.control.CommandRunner
import com.pamn.playground.control.RedoCommand
import com.pamn.playground.control.ResetCounterCommand
import com.pamn.playground.control.SetCapturePreviewCommand
import com.pamn.playground.control.SetPickedImageCommand
import com.pamn.playground.control.UndoCommand
import com.pamn.playground.model.AppState
import com.pamn.playground.sharedKernel.AppStateStore
import com.pamn.playground.sharedKernel.HistoryStore
import com.pamn.playground.sharedKernel.Router
import com.pamn.playground.ui.CounterView
import com.pamn.playground.view.MediaView
import java.io.ByteArrayOutputStream
import kotlin.collections.mapOf


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRoot() {
    val stateStore = remember { AppStateStore(AppState()) }
    val historyStore = remember { HistoryStore(maxSize = 50) }

    val snackbarHostState = remember { SnackbarHostState() }
    var errorText by remember { mutableStateOf<String?>(null) }
    val runner = remember { CommandRunner(stateStore, historyStore) }
    LaunchedEffect(errorText) {
        errorText?.let { snackbarHostState.showSnackbar(it); errorText = null }
    }

    val appState by stateStore.state().collectAsState()

    val context = LocalContext.current
    val haptics = LocalHapticFeedback.current

    val pickPhoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { runner.run(SetPickedImageCommand(it)) }
    }

    val takePreview = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bmp: Bitmap? ->
        bmp?.let {
            val bos = ByteArrayOutputStream().apply { it.compress(Bitmap.CompressFormat.PNG, 100, this) }
            runner.run(SetCapturePreviewCommand(bos.toByteArray()))
        }
    }

    val commands = remember(runner, appState) {
        mapOf(
            "counter:inc+1" to { runner.run(AddCounterCommand(+1)) },
            "counter:inc-1" to { runner.run(AddCounterCommand(-1)) },
            "counter:reset"  to { runner.run(ResetCounterCommand) },
            "undo" to { runner.run(UndoCommand) },
            "redo" to { runner.run(RedoCommand) },
            "android:share" to {
                val text = "Mi contador vale ${appState.counter.value}."
                val intent = Intent(Intent.ACTION_SEND)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_TEXT, text)
                context.startActivity(
                    Intent.createChooser(intent, "Compartir con…")
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            },

            "android:copyCounter" to {
                val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cm.setPrimaryClip(ClipData.newPlainText("counter", appState.counter.value.toString()))
                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            },

            "media:pick" to {
                pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },

            "media:capture" to { takePreview.launch(null) }
        )
    }

    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    Scaffold(
        topBar = { TopAppBar(title = { Text("Playground") }) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == Router.Counter.route,
                    onClick = { nav.navigate(Router.Counter.route) { launchSingleTop = true } },
                    icon = { Icon(Icons.Filled.Calculate, contentDescription = null) },
                    label = { Text(Router.Counter.label) }
                )
                NavigationBarItem(
                    selected = currentRoute == Router.Other.route,
                    onClick = { nav.navigate(Router.Other.route) { launchSingleTop = true } },
                    icon = { Icon(Icons.Filled.List, contentDescription = null) },
                    label = { Text(Router.Other.label) }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = Router.Counter.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Router.Counter.route) {
                Column(Modifier.fillMaxSize().padding(16.dp)) {
                    CounterView(
                        value = appState.counter.value,
                        commands = commands,
                        canUndo = historyStore.canUndo(),
                        canRedo = historyStore.canRedo()
                    )
                }
            }
            composable(Router.Other.route) {
                MediaView(stateStore.snapshot(), commands)
            }
        }
    }
}