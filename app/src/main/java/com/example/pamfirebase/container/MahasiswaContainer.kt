package com.example.pamfirebase.container

import com.example.pamfirebase.repositoy.MahasiswaRepository
import com.example.pamfirebase.repositoy.NetworkMahasiswa
import com.google.firebase.firestore.FirebaseFirestore

interface AppContainer{
    val MahasiswaRepository:MahasiswaRepository
}
class MahasiswaContainer : AppContainer{
    private val firebase: FirebaseFirestore = FirebaseFirestore.getInstance()

    override val mahasiswaRepository: MahasiswaRepository by lazy {
        NetworkMahasiswaRepository(firebase)
    }

}