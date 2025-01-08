package com.example.pamfirebase

class MahasiswaApplications: Application(){
    lateinit var container: AppContainer
    override fun onCreate(){
        super.onCreate()
        container = MahasiswaContainer()
    }
}