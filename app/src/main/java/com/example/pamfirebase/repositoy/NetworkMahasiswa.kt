package com.example.pamfirebase.repositoy

import com.example.pamfirebase.model.Mahasiswa
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NetworkMahasiswaRespository (
    private val firestore: FirebaseFirestore
): MahasiswaRepository {
    override suspend fun getAllMahasiswa(): Flow<List<Mahasiswa>> = callbackFlow {

        val mhsCollection = firestore.collection("Mahasiswa")
            .orderBy("nim", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (value != null) {
                    val mhsList = value.documents.mapNotNull {
                        it.toObject(Mahasiswa::class.java)
                    }
                    trySend(mhsList)
                }
            }
    }

    override suspend fun getMahasiswaByID(nim: String): Flow<Mahasiswa> {
        TODO("Not yet implemented")
    }

    override suspend fun insertMahasiswa(mahasiswa: Mahasiswa) {
        TODO("Not yet implemented")
    }

    override suspend fun updateMahasiswa(nim: String, mahasiswa: Mahasiswa) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMahasiswa(nim: String) {
        TODO("Not yet implemented")
    }
}


