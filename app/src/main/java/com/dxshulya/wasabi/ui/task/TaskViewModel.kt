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
import com.dxshulya.wasabi.model.Task
import com.dxshulya.wasabi.repository.TaskRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
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

    @Inject
    lateinit var taskRepository: TaskRepository

    @Inject
    lateinit var sharedPreference: SharedPreference

    fun getTasks() {
        taskRepository.getTasks()
            .observeOn(AndroidSchedulers.mainThread())
            .map { it }
            .cachedIn(viewModelScope)
            .subscribe({
                _tasks.value = it
            }){
                Log.e("TASK_PAGING", it.message.toString())
            }
    }

    fun getTotalCount() {
        taskRepository.getTotalCount()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                sharedPreference.totalCount = it.totalCount
            }, {
                Log.e("TOTAL_COUNT", it.message.toString())
            })
    }
}