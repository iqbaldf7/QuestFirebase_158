package com.example.pamfirebase.ui.theme.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pamfirebase.model.Mahasiswa
import com.example.pamfirebase.repositoy.MahasiswaRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

sealed class HomeUiState{
    data class Success(val mhs: List<Mahasiswa>): HomeUiState()
    data class Error(val exception: Throwable): HomeUiState()
    object Loading: HomeUiState()

}
class HomeViewModel (
    private val mhs:  MahasiswaRepository
): ViewModel(){
    var mhsUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set
    init {
        getMhs()

    }
    fun getMhs(){
        viewModelScope.launch {
            mhs.getAllMahasiswa()
                .onStart {
                    mhsUiState = HomeUiState.Loading
                }
                .catch {
                    mhsUiState = HomeUiState.Error(it)
                }
                .collect{
                    mhsUiState = if (it.isEmpty()){
                        HomeUiState.Error(Exception("Data Kosong"))
                    } else {
                        HomeUiState.Success(it)
                    }
                }
        }
    }
}
