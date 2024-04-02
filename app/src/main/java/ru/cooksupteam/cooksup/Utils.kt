package ru.cooksupteam.cooksup

import java.util.Locale

val regex = Regex("[^А-Яа-яёЁ ]")

fun String?.toIntOrDefault(default: Int) = this?.toIntOrNull() ?: default

fun Double.autoformat(): String =
    if (this.toLong().toDouble() == this) {
        "%d".format(Locale.ENGLISH, this.toLong())
    } else {
        with(Math.abs(this)) {
            when {
                this == 0.0 -> "%.0f"
                this < 0.1f -> "%.5f"
                this < 1f -> "%.4f"
                this < 10f -> "%.3f"
                this < 100f -> "%.2f"
                this < 1000f -> "%.1f"
                else -> "%.0f"
            }.format(Locale.ENGLISH, this@autoformat)
        }
    }
