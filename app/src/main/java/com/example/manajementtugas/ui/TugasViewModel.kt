package com.example.manajementtugas.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajementtugas.data.AppDatabase
import com.example.manajementtugas.data.StatusTugas
import com.example.manajementtugas.data.Tugas
import com.example.manajementtugas.data.TugasRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DaftarUiState(
    val tugas: List<Tugas> = emptyList(),
    val filterStatus: StatusTugas? = null
)

class TugasViewModel(application: Application) : AndroidViewModel(application) {

    private val repository =
        TugasRepository(AppDatabase.getInstance(application).tugasDao())

    private val filterStatus = MutableStateFlow<StatusTugas?>(null)
    private val kataKunci = MutableStateFlow("")

    // UI state disimpan di sini, bukan di composable
    val uiState: StateFlow<DaftarUiState> = combine(
        repository.semuaTugas,
        filterStatus,
        kataKunci
    ) { daftar, status, kunci ->
        val hasil = daftar.filter { t ->
            val cocokStatus = status == null || t.status == status
            val cocokKunci = kunci.isBlank() ||
                    t.judul.contains(kunci, ignoreCase = true) ||
                    t.mataKuliah.contains(kunci, ignoreCase = true)
            cocokStatus && cocokKunci
        }
        DaftarUiState(tugas = hasil, filterStatus = status)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DaftarUiState()
    )

    fun setFilter(status: StatusTugas?) {
        filterStatus.value = status
    }

    fun setKataKunci(kunci: String) {
        kataKunci.value = kunci
    }

    fun ambilTugas(id: Int): Flow<Tugas?> = repository.tugasById(id)

    fun simpan(tugas: Tugas) {
        viewModelScope.launch {
            if (tugas.id == 0) repository.tambah(tugas) else repository.ubah(tugas)
        }
    }

    fun hapus(tugas: Tugas) {
        viewModelScope.launch { repository.hapus(tugas) }
    }

    fun ubahStatus(tugas: Tugas, status: StatusTugas) {
        viewModelScope.launch { repository.ubah(tugas.copy(status = status)) }
    }
}