package com.dxshulya.wasabi

import android.app.Application
import com.dxshulya.wasabi.datastore.SharedPreference
import com.dxshulya.wasabi.di.*

class App: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        app = this

        val sharedPreference = SharedPreference(this)

        appComponent = DaggerAppComponent.builder()
            .networkModule(NetworkModule(sharedPreference.token))
            .repositoryModule(RepositoryModule())
            .appModule(AppModule(this))
            .build()
    }

    companion object {
        private lateinit var app: App

        @Synchronized
        fun getInstance(): App {
            return app
        }
    }
}