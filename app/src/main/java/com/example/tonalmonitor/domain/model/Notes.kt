package com.example.tonalmonitor.domain.model

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
        fun findFromFrequency(freq: Double): Pair<Notes, Int> {
            val odds: Array<Double> = Notes.values().map { freq / it.startFrequency }.toTypedArray()
            val octaves = odds.map { Pair(it.toInt() /*Octave*/, it - it.toInt()) /*Remain*/ }
            val noteInx: Int = octaves.indexOf(
                octaves.find { value ->
                    value.second == octaves.minOfOrNull { it.second }
                }
            )

            return Pair(Notes.values()[noteInx], octaves[noteInx].first)
        }

    }
}