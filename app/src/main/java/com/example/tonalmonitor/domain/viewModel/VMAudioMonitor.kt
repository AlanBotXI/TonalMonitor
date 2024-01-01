package com.example.tonalmonitor.domain.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class VMAudioMonitor: ViewModel() {
    val isStarted: MutableState<Boolean> = mutableStateOf(false)

    val histogram: MutableState<List<String>> = mutableStateOf(listOf())

    val permissionDenied: MutableState<Boolean> = mutableStateOf(false)

    fun start() {
        isStarted.value = true
    }

    fun stop() {
        isStarted.value = false
    }

    fun getHistogram(hist: Map<Int, Int>) {
        viewModelScope.launch {
            histogram.value = mutableListOf<String>().apply {
                hist.forEach{
                    add(
                        "Frequency: ${it.key}; Amplitude: ${it.value}"
                    )
                }
            }
        }
    }

    fun onPermissionDenied() {
        permissionDenied.value = true
    }

}