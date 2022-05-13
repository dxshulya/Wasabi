package com.dxshulya.wasabi

import android.util.Log
import androidx.lifecycle.ViewModel
import com.dxshulya.wasabi.data.datastore.SharedPreference
import com.dxshulya.wasabi.data.repository.TaskRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class MainActivityViewModel : ViewModel() {

    init {
        App.getInstance().appComponent.inject(this)
    }

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var taskRepository: TaskRepository

    fun changeMode(isNight: Boolean) {
        sharedPreference.isDarkMode = isNight
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