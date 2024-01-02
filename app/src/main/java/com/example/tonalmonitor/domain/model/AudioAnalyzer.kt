package com.example.tonalmonitor.domain.model

import androidx.annotation.RequiresPermission
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AudioAnalyzer {

    private val p0: Int = 22050
    private val p1: Int = 1024
    private lateinit var dispatcher: AudioDispatcher

    @RequiresPermission("android.permission.RECORD_AUDIO")
    fun startMonitoring(scope: CoroutineScope, onNoteDetected: (Pair<Notes, Int>?) -> Unit) {
        var i = 0
        val pdh = PitchDetectionHandler { result, _ ->
            i++
            if (i != 10 ) return@PitchDetectionHandler
            val pitch: Float = result.pitch
            onNoteDetected(
                Notes.findFromFrequency(pitch)
            )
            i = 0
        }

        val pitchProcessor: AudioProcessor = PitchProcessor(
            PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050f, 1024, pdh
        )
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(p0, p1, 0)

        dispatcher.addAudioProcessor(pitchProcessor)
        scope.launch(Dispatchers.Default) {
            dispatcher.run()
        }

    }

    fun stopMonitoring() {
        dispatcher.stop()
    }

}
