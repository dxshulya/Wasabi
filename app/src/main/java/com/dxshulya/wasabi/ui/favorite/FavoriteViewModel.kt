package com.dxshulya.wasabi.ui.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dxshulya.wasabi.App
import com.dxshulya.wasabi.datastore.SharedPreference
import com.dxshulya.wasabi.model.Favorites
import com.dxshulya.wasabi.repository.TaskRepository
import javax.inject.Inject

class FavoriteViewModel : ViewModel() {

    init {
        App.getInstance().appComponent.inject(this)
        getFavorites()
    }

    @Inject
    lateinit var taskRepository: TaskRepository

    @Inject
    lateinit var sharedPreference: SharedPreference

    private var _favorites = MutableLiveData<List<Favorites.Favorite>>()
    val favorites: LiveData<List<Favorites.Favorite>>
        get() = _favorites

    fun getFavorites() {
        taskRepository.getFavorites(10, 1)
            .subscribe({
                _favorites.value = it.array
                sharedPreference.totalPage = it.totalPage
                sharedPreference.totalCount = it.totalCount
            }, {
                Log.e("FVM", it.message.toString())
            })
    }
}