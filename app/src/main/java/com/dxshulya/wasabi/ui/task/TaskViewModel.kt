package com.dxshulya.wasabi.ui.task

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.dxshulya.wasabi.App
import com.dxshulya.wasabi.datastore.SharedPreference
import com.dxshulya.wasabi.model.Authorization
import com.dxshulya.wasabi.model.Task
import com.dxshulya.wasabi.model.TotalCount
import com.dxshulya.wasabi.repository.TaskRepository
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import retrofit2.HttpException
import javax.inject.Inject

class TaskViewModel : ViewModel() {

    init {
        App.getInstance().appComponent.inject(this)
        getTasks()
        getTotalCount()
    }

    private var _tasks = MutableLiveData<PagingData<Task>>()
    val tasks: LiveData<PagingData<Task>>
        get() = _tasks

    private var _favoriteLiveData = MutableLiveData<Authorization>()
    val favoriteLiveData: LiveData<Authorization>
        get() = _favoriteLiveData

    @Inject
    lateinit var taskRepository: TaskRepository

    @Inject
    lateinit var sharedPreference: SharedPreference

    fun getTasks() {
        taskRepository.getTasks()
            .observeOn(AndroidSchedulers.mainThread())
            .map { it }
            .cachedIn(viewModelScope)
            .subscribe {
                _tasks.value = it
            }
    }

    fun postFavorite() {
        val favorite = Task("123", "123", "123", "123")
        taskRepository.postFavorite(favorite)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _favoriteLiveData.value = it
            }, {
                if (it is HttpException) {
                    val body = it.response()?.errorBody()
                    val gson = Gson()
                    val adapter: TypeAdapter<Authorization> =
                        gson.getAdapter(Authorization::class.java)
                    val error: Authorization = adapter.fromJson(body?.string())
                    _favoriteLiveData.value = error
                }
            })
    }

    private fun getTotalCount() {
        taskRepository.getTotalCount()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                sharedPreference.totalCount = it.totalCount
            }, {
                Log.e("TOTAL_COUNT", it.message.toString())
            })
    }
}