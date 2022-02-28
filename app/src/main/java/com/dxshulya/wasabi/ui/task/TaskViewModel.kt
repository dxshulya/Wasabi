package com.dxshulya.wasabi.ui.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dxshulya.wasabi.App
import com.dxshulya.wasabi.datastore.SharedPreference
import com.dxshulya.wasabi.model.Task
import com.dxshulya.wasabi.repository.TaskRepository
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

    private var _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>>
        get() = _tasks

    fun getTasks() {
        val count = 10
        taskRepository.getTasks("Bearer " + sharedPreference.token, count, 1)
            .subscribe {
                _tasks.value = it
            }
    }
}