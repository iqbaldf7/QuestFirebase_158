package com.example.pamfirebase.ui.theme.viewmodel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pamfirebase.MahasiswaApplications

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer { HomeViewModel(AplikasiMahasiswa().container.mahasiswaRepository) }
        initializer { InsertViewModel(AplikasiMahasiswa().container.mahasiswaRepository) }
    }
}

fun CreationExtras.AplikasiMahasiswa(): MahasiswaApplications =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MahasiswaApplications)