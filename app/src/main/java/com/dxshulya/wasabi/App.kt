package com.dxshulya.wasabi

import android.app.Application
import com.dxshulya.wasabi.data.datastore.SharedPreference
import com.dxshulya.wasabi.core.di.*

class App: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        app = this

        val sharedPreference = SharedPreference(this)

        appComponent = DaggerAppComponent.builder()
            .networkModule(NetworkModule(sharedPreference))
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