package com.example.tonalmonitor.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.example.tonalmonitor.domain.viewModel.VMAudioMonitor
import com.example.tonalmonitor.ui.utils.ABXFloatingButton
import com.example.tonalmonitor.ui.utils.RequestAndroidPermission
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TonalMonitorScreen(viewModel: VMAudioMonitor) {

    Scaffold(floatingActionButton = {
        MonitorFloatingButton(viewModel)
    }, topBar = {
        TopAppBar(title = {
            Text(text = "Welcome",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        })
    }) {
        Contents(viewModel, Modifier.padding(it))
    }

}

@SuppressLint("MissingPermission")  //It is checked, but using the accompannist permission dep
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MonitorFloatingButton(viewModel: VMAudioMonitor) {
    val context: Context = LocalContext.current
    var askForPermissionAndNavigate: Boolean by remember { mutableStateOf(false) }

    if (viewModel.isStarted.value) {
        ABXFloatingButton(
            onClick = {
                viewModel.stop()
                Toast.makeText(context, "Stopping...",Toast.LENGTH_LONG).show()
            }, contentDescription = "Stop monitoring",
            imageVector = Icons.Default.Stop
        )
    } else {
        ABXFloatingButton(
            onClick = {
                askForPermissionAndNavigate = true
            }, contentDescription = "Start monitoring",
            imageVector = Icons.Default.PlayArrow
        )
    }

    if (askForPermissionAndNavigate) {
        val permissionState: PermissionState = rememberPermissionState(
            permission = Manifest.permission.RECORD_AUDIO
        )
        RequestAndroidPermission(
            permissionState = permissionState
        )
        when {
            permissionState.hasPermission -> {
                viewModel.start()
                askForPermissionAndNavigate = false
            }
            else -> {
                viewModel.onPermissionDenied()
            }
        }
    }
}

@Composable
private fun Contents(viewModel: VMAudioMonitor, modifier: Modifier) {
    Box(modifier = modifier) {
        if (viewModel.permissionDenied.value) {

            Text(text = "You should provide microphone permission")

        } else {
            viewModel.note.value?.let {

                Text(text = "${it.first.noteName} ${it.second}")

            } ?: Text(text = "Nothing here!")
        }
    }

}

