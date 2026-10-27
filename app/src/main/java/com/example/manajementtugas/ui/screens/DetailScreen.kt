@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.manajementtugas.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.manajementtugas.data.StatusTugas
import com.example.manajementtugas.data.Tugas
import com.example.manajementtugas.util.formatTanggal

@Composable
fun DetailScreen(
    tugas: Tugas?,
    onKembali: () -> Unit,
    onEdit: () -> Unit,
    onHapus: () -> Unit,
    onUbahStatus: (StatusTugas) -> Unit
) {
    var konfirmasiHapus by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Tugas") },
                navigationIcon = {
                    IconButton(onClick = onKembali) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    if (tugas != null) {
                        IconButton(onClick = onEdit) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = { konfirmasiHapus = true }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Hapus")
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (tugas == null) {
            Box(
                Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Memuat...")
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(tugas.judul, style = MaterialTheme.typography.headlineSmall)
                Baris("Mata kuliah", tugas.mataKuliah)
                Baris("Deskripsi", tugas.deskripsi.ifBlank { "-" })
                Baris("Deadline", formatTanggal(tugas.deadline))
                Baris("Prioritas", tugas.prioritas.label)

                Text(
                    "Status",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatusTugas.entries.forEach { s ->
                        FilterChip(
                            selected = tugas.status == s,
                            onClick = { onUbahStatus(s) },
                            label = { Text(s.label) }
                        )
                    }
                }
            }
        }
    }

    if (konfirmasiHapus && tugas != null) {
        AlertDialog(
            onDismissRequest = { konfirmasiHapus = false },
            title = { Text("Hapus tugas?") },
            text = { Text("Tugas \"${tugas.judul}\" akan dihapus permanen.") },
            confirmButton = {
                TextButton(onClick = {
                    konfirmasiHapus = false
                    onHapus()
                }) { Text("Hapus") }
            },
            dismissButton = {
                TextButton(onClick = { konfirmasiHapus = false }) { Text("Batal") }
            }
        )
    }
}

@Composable
private fun Baris(label: String, nilai: String) {
    Column {
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(nilai, style = MaterialTheme.typography.bodyLarge)
    }
}