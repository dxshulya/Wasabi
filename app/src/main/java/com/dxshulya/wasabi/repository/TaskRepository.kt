package com.dxshulya.wasabi.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dxshulya.wasabi.data.Api
import com.dxshulya.wasabi.model.Authorization
import com.dxshulya.wasabi.model.Favorites
import com.dxshulya.wasabi.model.Task
import com.dxshulya.wasabi.model.User
import com.dxshulya.wasabi.paging.TaskPagingSource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class TaskRepository(private val api: Api) {

    fun registrationUser(user: User): Observable<Authorization> {
        return api.postRegistration(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun loginUser(user: User): Observable<Authorization> {
        return api.postLogin(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = 1, enablePlaceholders = false)
    }

    fun getTasks(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<Task>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {TaskPagingSource(api)}
        ).flow
    }

    fun postFavorite(favorite: Task): Observable<Authorization> {
        return api.postFavorite(favorite)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getFavorites(count: Int, page: Int): Observable<Favorites> {
        return api.getFavorites(count, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun deleteFavorite(id: Int): Observable<Authorization> {
        return api.deleteFavorite(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}