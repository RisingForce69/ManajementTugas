package com.example.manajementtugas.data


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TugasDao {

    @Query("SELECT * FROM tugas ORDER BY deadline ASC")
    fun semuaTugas(): Flow<List<Tugas>>

    @Query("SELECT * FROM tugas WHERE id = :id")
    fun tugasById(id: Int): Flow<Tugas?>

    @Insert
    suspend fun tambah(tugas: Tugas)

    @Update
    suspend fun ubah(tugas: Tugas)

    @Delete
    suspend fun hapus(tugas: Tugas)
}