@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.manajementtugas.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.manajementtugas.ui.DaftarUiState
import com.example.manajementtugas.util.InfoDeadline
import com.example.manajementtugas.util.formatTanggal
import com.example.manajementtugas.util.infoDeadline

@Composable
fun DaftarScreen(
    uiState: DaftarUiState,
    onCari: (String) -> Unit,
    onFilter: (StatusTugas?) -> Unit,
    onTambah: () -> Unit,
    onDetail: (Int) -> Unit
) {
    var kunci by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Manajemen Tugas Mahasiswa") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onTambah) {
                Icon(Icons.Filled.Add, contentDescription = "Tambah tugas")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = kunci,
                onValueChange = {
                    kunci = it
                    onCari(it)
                },
                label = { Text("Cari judul / mata kuliah") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = uiState.filterStatus == null,
                    onClick = { onFilter(null) },
                    label = { Text("Semua") }
                )
                StatusTugas.entries.forEach { status ->
                    FilterChip(
                        selected = uiState.filterStatus == status,
                        onClick = { onFilter(status) },
                        label = { Text(status.label) }
                    )
                }
            }

            if (uiState.tugas.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tidak ada tugas")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.tugas, key = { it.id }) { tugas ->
                        TugasCard(tugas = tugas, onClick = { onDetail(tugas.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun TugasCard(tugas: Tugas, onClick: () -> Unit) {
    val info = infoDeadline(tugas.deadline, tugas.status == StatusTugas.SELESAI)

    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(tugas.judul, style = MaterialTheme.typography.titleMedium)
            Text(tugas.mataKuliah, style = MaterialTheme.typography.bodyMedium)
            Text(
                "Deadline: ${formatTanggal(tugas.deadline)}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Prioritas: ${tugas.prioritas.label}  |  Status: ${tugas.status.label}",
                style = MaterialTheme.typography.bodySmall
            )
            when (info) {
                InfoDeadline.TERLAMBAT -> Text(
                    "Terlambat",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium
                )
                InfoDeadline.SEGERA -> Text(
                    "Deadline dekat",
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.labelMedium
                )
                InfoDeadline.NORMAL -> Unit
            }
        }
    }
}