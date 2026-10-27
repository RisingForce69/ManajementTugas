package com.example.manajementtugas.navigation


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.manajementtugas.ui.TugasViewModel
import com.example.manajementtugas.ui.screens.DaftarScreen
import com.example.manajementtugas.ui.screens.DetailScreen
import com.example.manajementtugas.ui.screens.FormScreen

object Rute {
    const val DAFTAR = "daftar"
    const val TAMBAH = "tambah"
    const val DETAIL = "detail/{id}"
    const val EDIT = "edit/{id}"

    fun detail(id: Int) = "detail/$id"
    fun edit(id: Int) = "edit/$id"
}

@Composable
fun AppNavigation(viewModel: TugasViewModel = viewModel()) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val argId = listOf(navArgument("id") { type = NavType.IntType })

    NavHost(navController = navController, startDestination = Rute.DAFTAR) {

        composable(Rute.DAFTAR) {
            DaftarScreen(
                uiState = uiState,
                onCari = viewModel::setKataKunci,
                onFilter = viewModel::setFilter,
                onTambah = { navController.navigate(Rute.TAMBAH) },
                onDetail = { id -> navController.navigate(Rute.detail(id)) }
            )
        }

        composable(Rute.TAMBAH) {
            FormScreen(
                judulHalaman = "Tambah Tugas",
                tugasAwal = null,
                onSimpan = { tugas ->
                    viewModel.simpan(tugas)
                    navController.popBackStack()
                },
                onKembali = { navController.popBackStack() }
            )
        }

        composable(Rute.DETAIL, arguments = argId) { entry ->
            val id = entry.arguments?.getInt("id") ?: return@composable
            val tugas by remember(id) { viewModel.ambilTugas(id) }
                .collectAsStateWithLifecycle(initialValue = null)

            DetailScreen(
                tugas = tugas,
                onKembali = { navController.popBackStack() },
                onEdit = { navController.navigate(Rute.edit(id)) },
                onHapus = {
                    tugas?.let { viewModel.hapus(it) }
                    navController.popBackStack()
                },
                onUbahStatus = { status ->
                    tugas?.let { viewModel.ubahStatus(it, status) }
                }
            )
        }

        composable(Rute.EDIT, arguments = argId) { entry ->
            val id = entry.arguments?.getInt("id") ?: return@composable
            val tugas by remember(id) { viewModel.ambilTugas(id) }
                .collectAsStateWithLifecycle(initialValue = null)

            val data = tugas
            if (data == null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                FormScreen(
                    judulHalaman = "Edit Tugas",
                    tugasAwal = data,
                    onSimpan = { diubah ->
                        viewModel.simpan(diubah)
                        navController.popBackStack()
                    },
                    onKembali = { navController.popBackStack() }
                )
            }
        }
    }
}