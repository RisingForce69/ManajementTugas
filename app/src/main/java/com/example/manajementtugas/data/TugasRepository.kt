package com.example.manajementtugas.data


import kotlinx.coroutines.flow.Flow

class TugasRepository(private val dao: TugasDao) {

    val semuaTugas: Flow<List<Tugas>> = dao.semuaTugas()

    fun tugasById(id: Int): Flow<Tugas?> = dao.tugasById(id)

    suspend fun tambah(tugas: Tugas) = dao.tambah(tugas)

    suspend fun ubah(tugas: Tugas) = dao.ubah(tugas)

    suspend fun hapus(tugas: Tugas) = dao.hapus(tugas)
}