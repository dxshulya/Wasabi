package com.dxshulya.wasabi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dxshulya.wasabi.datastore.SharedPreference
import com.dxshulya.wasabi.repository.TaskRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class MainActivityViewModel : ViewModel() {

    init {
        App.getInstance().appComponent.inject(this)
        getTotalCount()
    }

    private var _countLiveData = MutableLiveData<String>()
    val countLiveData: LiveData<String> = _countLiveData

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
                _countLiveData.value = it.totalCount.toString()
            }, {
                Log.e("TOTAL_COUNT", it.message.toString())
            })
    }
}