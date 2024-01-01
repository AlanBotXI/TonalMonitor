package com.example.tonalmonitor.domain.model

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Process
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


class AudioAnalyzer {
    private var audioRecord: AudioRecord? = null
    private val bufferSize: Int
    private val sampleRate: Int = 4000
    private val channelConfig: Int = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat: Int = AudioFormat.ENCODING_PCM_16BIT

    init {
        bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
    }

    @RequiresPermission("android.permission.RECORD_AUDIO")
    fun startMonitoring(scope: CoroutineScope, onAudioData: (Pair<Notes, Int>) -> Unit) {
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            channelConfig,
            audioFormat,
            bufferSize
        )

        audioRecord?.startRecording()

        scope.launch(Dispatchers.IO) {
            Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO)

            val audioData = ShortArray(bufferSize / 2)
            var counter: Int = 0

            while (audioRecord?.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                val bytesRead = audioRecord?.read(audioData, 0, audioData.size)

                if (bytesRead != null && bytesRead > 0 && counter <= 10) {
                    onAudioData(
                        findNote(audioData)
                    )
//                    counter = 0
                }
                counter++
            }

            audioRecord?.stop()
            audioRecord?.release()
//            audioRecord = null
        }
    }

    fun stopMonitoring() {
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }

    private fun findNote(input: ShortArray): Pair<Notes, Int> {
        return Notes.findFromFrequency(
            findDominantFrequency(
                FFT.fft(
                    input.map { Complex(it.toDouble(), 0.toDouble()) }.toTypedArray()
                )
            )
        )
    }

    private fun findDominantFrequency(transform: Array<Complex>): Double {

        // Calculate magnitude spectrum
        val magnitudeSpectrum: DoubleArray = transform.map { it.re.absoluteValue }.toDoubleArray()

        // Find the index of the maximum magnitude
        val indexOfMaxMagnitude =
            magnitudeSpectrum.indexOfFirst { it == magnitudeSpectrum.maxOrNull() }

        // Calculate the corresponding frequency in Hertz

        return indexOfMaxMagnitude * sampleRate.toDouble() / transform.size
    }

}

/*
    private fun generateHistogram(audioData: ShortArray): Map<Int, Int> {
        val histogram = mutableMapOf<Int, Int>()

        for (amplitude in audioData) {
            // Convert amplitude to non-negative value
            val absAmplitude = abs(amplitude.toInt())

            // Increment the count for the given amplitude value
            histogram[absAmplitude] = histogram.getOrDefault(absAmplitude, 0) + 1
        }

        return histogram
    }
*/