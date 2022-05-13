package com.dxshulya.wasabi.ui.item

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dxshulya.wasabi.App
import com.dxshulya.wasabi.data.datastore.SharedPreference
import com.dxshulya.wasabi.data.model.Authorization
import com.dxshulya.wasabi.data.model.Favorites
import com.dxshulya.wasabi.data.model.Task
import com.dxshulya.wasabi.data.repository.TaskRepository
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import retrofit2.HttpException
import javax.inject.Inject

class FavoriteItemViewModel(private val favorite: Favorites.Favorite) : ViewModel() {

    init {
        App.getInstance().appComponent.inject(this)
    }

    var isPressed = false

    @Inject
    lateinit var taskRepository: TaskRepository

    @Inject
    lateinit var sharedPreference: SharedPreference

    private var _deleteFavoriteLiveData = MutableLiveData<Authorization>()
    val deleteFavoriteLiveData: LiveData<Authorization>
        get() = _deleteFavoriteLiveData

    fun getTotalCount() {
        taskRepository.getTotalCount()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                sharedPreference.totalCount = it.totalCount
            }, {
                Log.e("TOTAL_COUNT", it.message.toString())
            })
    }

    fun deleteOrPostFavorite() {
        isPressed = !isPressed
        favorite.isDisliked = isPressed

        if(isPressed) {
            taskRepository.deleteFavorite(favorite.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _deleteFavoriteLiveData.value = it
                }, {
                    if (it is HttpException) {
                        val body = it.response()?.errorBody()
                        val gson = Gson()
                        val adapter: TypeAdapter<Authorization> =
                            gson.getAdapter(Authorization::class.java)
                        val error: Authorization = adapter.fromJson(body?.string())
                        _deleteFavoriteLiveData.value = error
                    }
                })
        }

        else if(!isPressed) {
            val task = Task(favorite.id, favorite.text, favorite.answer, favorite.formula)
            taskRepository.postFavorite(task)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _deleteFavoriteLiveData.value = it
                }, {
                    if (it is HttpException) {
                        val body = it.response()?.errorBody()
                        val gson = Gson()
                        val adapter: TypeAdapter<Authorization> =
                            gson.getAdapter(Authorization::class.java)
                        val error: Authorization = adapter.fromJson(body?.string())
                        _deleteFavoriteLiveData.value = error
                    }
                })
        }

    }
}