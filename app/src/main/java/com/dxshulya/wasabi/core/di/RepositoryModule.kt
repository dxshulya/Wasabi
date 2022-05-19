package com.dxshulya.wasabi.core.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import com.dxshulya.wasabi.data.Api
import com.dxshulya.wasabi.domain.datastore.ISharedPreference
import com.dxshulya.wasabi.domain.datastore.SharedPreference
import com.dxshulya.wasabi.data.repository.ITaskRepository
import com.dxshulya.wasabi.data.repository.TaskRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideISharedPreference(sharedPreference: SharedPreference): ISharedPreference = sharedPreference

    @ExperimentalPagingApi
    @Provides
    @Singleton
    fun provideITaskRepository(taskRepository: TaskRepository): ITaskRepository = taskRepository

    @ExperimentalPagingApi
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