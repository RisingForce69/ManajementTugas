package com.example.manajementtugas.data


import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Prioritas(val label: String) {
    RENDAH("Rendah"),
    SEDANG("Sedang"),
    TINGGI("Tinggi")
}

enum class StatusTugas(val label: String) {
    BELUM("Belum"),
    PROSES("Proses"),
    SELESAI("Selesai")
}

@Entity(tableName = "tugas")
data class Tugas(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mataKuliah: String,
    val judul: String,
    val deskripsi: String,
    val deadline: Long, // epoch millis (UTC)
    val prioritas: Prioritas,
    val status: StatusTugas
)