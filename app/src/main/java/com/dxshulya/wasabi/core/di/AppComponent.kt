package com.dxshulya.wasabi.core.di

import com.dxshulya.wasabi.MainActivityViewModel
import com.dxshulya.wasabi.data.repository.TaskRepository
import com.dxshulya.wasabi.ui.favorite.FavoriteViewModel
import com.dxshulya.wasabi.ui.item.FavoriteItemViewModel
import com.dxshulya.wasabi.ui.item.TaskItemViewModel
import com.dxshulya.wasabi.ui.login.LoginViewModel
import com.dxshulya.wasabi.ui.registration.RegistrationViewModel
import com.dxshulya.wasabi.ui.task.TaskViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class), (RepositoryModule::class), (AppModule::class)])
interface AppComponent {
    fun inject(taskRepository: TaskRepository)
    fun inject(taskViewModel: TaskViewModel)
    fun inject(registrationViewModel: RegistrationViewModel)
    fun inject(loginViewModel: LoginViewModel)
    fun inject(favoriteViewModel: FavoriteViewModel)
    fun inject(taskItemViewModel: TaskItemViewModel)
    fun inject(favoriteItemViewModel: FavoriteItemViewModel)
    fun inject (mainActivityViewModel: MainActivityViewModel)
}