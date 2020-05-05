@file:Suppress("SpellCheckingInspection")

package xyz.k4czp3r.trustnotify.models

data class DelayAfterDetectType(val id: Int, val value: Int, val readable: String)

val DADT_0 = DelayAfterDetectType(0, 0, "0s")
val DADT_250 = DelayAfterDetectType(1, 250, "0.25s")
val DADT_500 = DelayAfterDetectType(1, 500, "0.5s")
val DADT_1000 = DelayAfterDetectType(1, 1000, "1s")
val DADT_1500 = DelayAfterDetectType(1, 1500, "1.5s")
val DADT_2000 = DelayAfterDetectType(1, 2000, "2s")


val DelayAfterDetectTypes = listOf(DADT_0, DADT_250, DADT_500, DADT_1000, DADT_1500, DADT_2000)