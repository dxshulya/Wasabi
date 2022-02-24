package com.dxshulya.wasabi.di

import com.dxshulya.wasabi.data.Api
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
}