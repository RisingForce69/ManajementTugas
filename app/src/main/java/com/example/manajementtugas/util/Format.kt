package com.example.manajementtugas.util


import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private const val SATU_HARI = 24L * 60 * 60 * 1000

enum class InfoDeadline { TERLAMBAT, SEGERA, NORMAL }

fun formatTanggal(millis: Long): String {
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
    // DatePicker Material3 mengembalikan tengah malam UTC
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(Date(millis))
}

fun infoDeadline(
    deadline: Long,
    selesai: Boolean,
    sekarang: Long = System.currentTimeMillis()
): InfoDeadline {
    if (selesai) return InfoDeadline.NORMAL
    return when {
        deadline + SATU_HARI < sekarang -> InfoDeadline.TERLAMBAT
        deadline - sekarang < 3 * SATU_HARI -> InfoDeadline.SEGERA
        else -> InfoDeadline.NORMAL
    }
}