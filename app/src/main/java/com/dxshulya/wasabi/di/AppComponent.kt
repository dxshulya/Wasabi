package com.dxshulya.wasabi.di

import androidx.paging.ExperimentalPagingApi
import com.dxshulya.wasabi.repository.TaskRepository
import com.dxshulya.wasabi.ui.favorite.FavoriteViewModel
import com.dxshulya.wasabi.ui.login.LoginViewModel
import com.dxshulya.wasabi.ui.registration.RegistrationViewModel
import com.dxshulya.wasabi.ui.task.TaskViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class), (RepositoryModule::class), (AppModule::class)])
interface AppComponent {
    @ExperimentalPagingApi
    fun inject(taskRepository: TaskRepository)
    @ExperimentalPagingApi
    fun inject(taskViewModel: TaskViewModel)
    @ExperimentalPagingApi
    fun inject(registrationViewModel: RegistrationViewModel)
    @ExperimentalPagingApi
    fun inject(loginViewModel: LoginViewModel)
    @ExperimentalPagingApi
    fun inject(favoriteViewModel: FavoriteViewModel)
}