@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.manajementtugas.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.manajementtugas.data.Prioritas
import com.example.manajementtugas.data.StatusTugas
import com.example.manajementtugas.data.Tugas
import com.example.manajementtugas.util.formatTanggal

/** Dipakai untuk Tambah (tugasAwal = null) dan Edit (tugasAwal terisi). */
@Composable
fun FormScreen(
    judulHalaman: String,
    tugasAwal: Tugas?,
    onSimpan: (Tugas) -> Unit,
    onKembali: () -> Unit
) {
    var mataKuliah by rememberSaveable { mutableStateOf(tugasAwal?.mataKuliah ?: "") }
    var judul by rememberSaveable { mutableStateOf(tugasAwal?.judul ?: "") }
    var deskripsi by rememberSaveable { mutableStateOf(tugasAwal?.deskripsi ?: "") }
    var deadline by rememberSaveable { mutableStateOf<Long?>(tugasAwal?.deadline) }
    var prioritas by rememberSaveable { mutableStateOf(tugasAwal?.prioritas ?: Prioritas.SEDANG) }
    var status by rememberSaveable { mutableStateOf(tugasAwal?.status ?: StatusTugas.BELUM) }
    var tampilPicker by rememberSaveable { mutableStateOf(false) }
    var sudahCoba by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(judulHalaman) },
                navigationIcon = {
                    IconButton(onClick = onKembali) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = mataKuliah,
                onValueChange = { mataKuliah = it },
                label = { Text("Mata kuliah") },
                singleLine = true,
                isError = sudahCoba && mataKuliah.isBlank(),
                supportingText = {
                    if (sudahCoba && mataKuliah.isBlank()) Text("Wajib diisi")
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = judul,
                onValueChange = { judul = it },
                label = { Text("Judul tugas") },
                singleLine = true,
                isError = sudahCoba && judul.isBlank(),
                supportingText = {
                    if (sudahCoba && judul.isBlank()) Text("Wajib diisi")
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = deskripsi,
                onValueChange = { deskripsi = it },
                label = { Text("Deskripsi") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Text("Deadline", style = MaterialTheme.typography.labelLarge)
            OutlinedButton(
                onClick = { tampilPicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(deadline?.let { formatTanggal(it) } ?: "Pilih tanggal")
            }
            if (sudahCoba && deadline == null) {
                Text(
                    "Deadline wajib dipilih",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text("Prioritas", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Prioritas.entries.forEach { p ->
                    FilterChip(
                        selected = prioritas == p,
                        onClick = { prioritas = p },
                        label = { Text(p.label) }
                    )
                }
            }

            Text("Status", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusTugas.entries.forEach { s ->
                    FilterChip(
                        selected = status == s,
                        onClick = { status = s },
                        label = { Text(s.label) }
                    )
                }
            }

            Button(
                onClick = {
                    sudahCoba = true
                    val dl = deadline
                    if (mataKuliah.isNotBlank() && judul.isNotBlank() && dl != null) {
                        onSimpan(
                            Tugas(
                                id = tugasAwal?.id ?: 0,
                                mataKuliah = mataKuliah.trim(),
                                judul = judul.trim(),
                                deskripsi = deskripsi.trim(),
                                deadline = dl,
                                prioritas = prioritas,
                                status = status
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simpan")
            }
        }
    }

    if (tampilPicker) {
        val pickerState = rememberDatePickerState(initialSelectedDateMillis = deadline)
        DatePickerDialog(
            onDismissRequest = { tampilPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    deadline = pickerState.selectedDateMillis
                    tampilPicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { tampilPicker = false }) { Text("Batal") }
            }
        ) {
            DatePicker(state = pickerState)
        }
    }
}