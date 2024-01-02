package com.example.tonalmonitor.domain.viewModel

import androidx.annotation.RequiresPermission
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tonalmonitor.domain.model.AudioAnalyzer
import com.example.tonalmonitor.domain.model.Notes

class VMAudioMonitor: ViewModel() {
    val isStarted: MutableState<Boolean> = mutableStateOf(false)

    val note: MutableState<Pair<Notes, Int>?> = mutableStateOf(null)

    val permissionDenied: MutableState<Boolean> = mutableStateOf(false)

    private val audioAnalizer: AudioAnalyzer = AudioAnalyzer()

    @RequiresPermission("android.permission.RECORD_AUDIO")
    fun start() {
        audioAnalizer.startMonitoring(viewModelScope) {
            note.value = it
        }
        isStarted.value = true
    }

    fun stop() {
        audioAnalizer.stopMonitoring()
        isStarted.value = false
    }

    fun onPermissionDenied() {
        permissionDenied.value = true
    }

}