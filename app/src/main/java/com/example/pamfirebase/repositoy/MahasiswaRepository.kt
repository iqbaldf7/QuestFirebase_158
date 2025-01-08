package com.example.pamfirebase.repositoy

import com.example.pamfirebase.model.Mahasiswa
import kotlinx.coroutines.flow.Flow

interface MahasiswaRepository {

    suspend fun getAllMahasiswa(): Flow<List<Mahasiswa>>

    suspend fun getMahasiswaByID(nim: String): Flow<Mahasiswa>

    suspend fun insertMahasiswa(mahasiswa: Mahasiswa)

    suspend fun updateMahasiswa(nim: String, mahasiswa: Mahasiswa)

    suspend fun deleteMahasiswa(nim: String)
}