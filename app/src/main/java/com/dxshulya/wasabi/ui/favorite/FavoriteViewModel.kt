package com.dxshulya.wasabi.ui.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.dxshulya.wasabi.App
import com.dxshulya.wasabi.data.datastore.SharedPreference
import com.dxshulya.wasabi.data.model.Favorites
import com.dxshulya.wasabi.data.repository.TaskRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
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

    private var _favorites = MutableLiveData<PagingData<Favorites.Favorite>>()
    val favorites: LiveData<PagingData<Favorites.Favorite>>
        get() = _favorites

    fun getFavorites() {
        taskRepository.getFavorites()
            .observeOn(AndroidSchedulers.mainThread())
            .map { it }
            .cachedIn(viewModelScope)
            .subscribe ({
                _favorites.value = it
            }){
                Log.e("FAV_PAGING", it.message.toString())
            }
    }
}