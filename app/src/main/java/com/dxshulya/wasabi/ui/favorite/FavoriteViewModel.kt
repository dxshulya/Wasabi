package com.dxshulya.wasabi.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dxshulya.wasabi.App
import com.dxshulya.wasabi.datastore.SharedPreference
import com.dxshulya.wasabi.repository.TaskRepository
import javax.inject.Inject

class FavoriteViewModel : ViewModel() {
    init {
        App.getInstance().appComponent.inject(this)
        //getFavorites()
    }

    @Inject
    lateinit var taskRepository: TaskRepository

    @Inject
    lateinit var sharedPreference: SharedPreference

//    private var _favorites = MutableLiveData<MutableList<Favorites.Favorite>>()
//    val favorites: LiveData<MutableList<Favorites.Favorite>>
//        get() = _favorites
//
//    fun getFavorites() {
//        taskRepository.getFavorites("Bearer " + sharedPreference.token, 10, 1)
//            .subscribe {
//                _favorites.value = it
//            }
//    }
}