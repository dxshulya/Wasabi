package com.dxshulya.wasabi

import android.app.Application
import com.dxshulya.wasabi.di.AppComponent
import com.dxshulya.wasabi.di.DaggerAppComponent
import com.dxshulya.wasabi.di.NetworkModule
import com.dxshulya.wasabi.di.RepositoryModule

class App: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        app = this

        appComponent = DaggerAppComponent.builder()
            .networkModule(NetworkModule())
            .repositoryModule(RepositoryModule())
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