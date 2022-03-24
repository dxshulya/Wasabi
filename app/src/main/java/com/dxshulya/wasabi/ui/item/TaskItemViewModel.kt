package com.dxshulya.wasabi.ui.item

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dxshulya.wasabi.App
import com.dxshulya.wasabi.datastore.SharedPreference
import com.dxshulya.wasabi.model.Authorization
import com.dxshulya.wasabi.model.Task
import com.dxshulya.wasabi.repository.TaskRepository
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import retrofit2.HttpException
import javax.inject.Inject

class TaskItemViewModel(private val task: Task) : ViewModel() {

    init {
        App.getInstance().appComponent.inject(this)
    }

    @Inject
    lateinit var taskRepository: TaskRepository

    @Inject
    lateinit var sharedPreference: SharedPreference

    private var _favoriteLiveData = MutableLiveData<Authorization>()
    val favoriteLiveData: LiveData<Authorization>
        get() = _favoriteLiveData

    fun postFavorite() {
        taskRepository.postFavorite(task)
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
}