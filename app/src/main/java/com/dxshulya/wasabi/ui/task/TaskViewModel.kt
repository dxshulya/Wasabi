package com.dxshulya.wasabi.ui.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dxshulya.wasabi.App
import com.dxshulya.wasabi.datastore.SharedPreference
import com.dxshulya.wasabi.model.Authorization
import com.dxshulya.wasabi.model.Favorites
import com.dxshulya.wasabi.model.Task
import com.dxshulya.wasabi.repository.TaskRepository
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import retrofit2.HttpException
import javax.inject.Inject

class TaskViewModel : ViewModel() {

    init {
        App.getInstance().appComponent.inject(this)
        getTasks()
    }

    @Inject
    lateinit var taskRepository: TaskRepository

    @Inject
    lateinit var sharedPreference: SharedPreference

    private lateinit var favorites: Favorites.Favorite

    private var _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>>
        get() = _tasks

    private var _favorite = MutableLiveData<Authorization>()
    val favorite: LiveData<Authorization>
        get() = _favorite


    fun getTasks() {
        val count = 10
        taskRepository.getTasks("Bearer " + sharedPreference.token, count, 1)
            .subscribe {
                _tasks.value = it
            }
    }

    fun postFavorite() {
        taskRepository.postFavorite("Bearer " + sharedPreference.token, Task("1", "Это", "приложение", "мое"))
            .subscribe({
                _favorite.value = it
            }, {
                if (it is HttpException) {
                    val body = it.response()?.errorBody()
                    val gson = Gson()
                    val adapter: TypeAdapter<Authorization> =
                        gson.getAdapter(Authorization::class.java)
                    val error: Authorization = adapter.fromJson(body?.string())
                    _favorite.value = error
                }
            })
    }

    fun deleteFavorite() {
        taskRepository.deleteFavorite("Bearer " + sharedPreference.token, favorites.id)
            .subscribe({
                _favorite.value = it
            }, {
                if (it is HttpException) {
                    val body = it.response()?.errorBody()
                    val gson = Gson()
                    val adapter: TypeAdapter<Authorization> =
                        gson.getAdapter(Authorization::class.java)
                    val error: Authorization = adapter.fromJson(body?.string())
                    _favorite.value = error
                }
            })
    }

}