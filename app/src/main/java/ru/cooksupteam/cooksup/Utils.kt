package ru.cooksupteam.cooksup

import android.text.TextUtils
import androidx.compose.ui.graphics.Color
import java.nio.charset.Charset
import java.util.Locale


fun getColorFromString(color: String): Color {
    val colorWithoutHash = color.substring(1)
    val r = colorWithoutHash.substring(0, 2).toInt(16)
    val g = colorWithoutHash.substring(2, 4).toInt(16)
    val b = colorWithoutHash.substring(4, 6).toInt(16)
    val a = colorWithoutHash.substring(6, 8).toInt(16)
    return Color(r, g, b, a)
}

val regex = Regex("[^А-Яа-яёЁ ]")

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}
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
fun String.toUTF8(): String {
    // Получаем экземпляр Charset для кодировки UTF-8
    val charset = Charset.forName("UTF-8")

    // Преобразуем строку из UTF-16 в UTF-8
    return this.toByteArray(charset).toString(charset)
}
