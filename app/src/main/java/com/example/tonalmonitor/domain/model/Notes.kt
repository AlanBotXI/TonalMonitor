package com.example.tonalmonitor.domain.model

import kotlin.math.absoluteValue

enum class Notes(val noteName: String, val startFrequency: Float) {
    C(noteName = "Do", startFrequency = 16.35F),
    CS(noteName = "Do sostenido", startFrequency = 17.32F),
    D(noteName = "Re", startFrequency = 18.35F),
    EF(noteName = "Mi bemol", startFrequency = 19.45F),
    E(noteName = "Mi", startFrequency = 20.6F),
    F(noteName = "Fa", startFrequency = 21.83F),
    FS(noteName = "Fa sostenido", startFrequency = 23.12F),
    G(noteName = "Sol", startFrequency = 24.5F),
    GS(noteName = "Sol sostenido", startFrequency = 25.96F),
    A(noteName = "La", startFrequency = 27.5F),
    BF(noteName = "Si bemol", startFrequency = 29.14F),
    B(noteName = "Si", startFrequency = 30.87F);

    companion object {
        fun findFromFrequency(freq: Float): Pair<Notes, Int>? {
            return try {
                if (freq < 15) throw Exception()
                val odds: List<Pair<Int, Float>> = Notes.values().map { note ->
                    val ratio = freq / note.startFrequency
                    Pair(ratio.toInt(), ratio - ratio.toInt())
                }
                val closestNote = odds.minByOrNull { it.second.absoluteValue }

                if (closestNote != null) {
                    Pair(Notes.values()[closestNote.first], closestNote.first)
                } else {
                    null
                }
            } catch (ex: Exception) {
                null
            }
        }

    }
}