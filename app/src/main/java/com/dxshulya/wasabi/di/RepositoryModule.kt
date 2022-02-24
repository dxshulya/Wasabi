package com.dxshulya.wasabi.di

import android.content.Context
import com.dxshulya.wasabi.data.Api
import com.dxshulya.wasabi.datastore.SharedPreference
import com.dxshulya.wasabi.repository.TaskRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideTaskRepository(api: Api): TaskRepository {
        return TaskRepository(api)
    }

    @Provides
    @Singleton
    fun provideSharedPreference(context: Context): SharedPreference {
        return SharedPreference(context)
    }
}