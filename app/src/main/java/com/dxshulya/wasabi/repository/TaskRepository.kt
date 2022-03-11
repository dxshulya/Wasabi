package com.dxshulya.wasabi.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.dxshulya.wasabi.data.Api
import com.dxshulya.wasabi.model.Authorization
import com.dxshulya.wasabi.model.Favorites
import com.dxshulya.wasabi.model.Task
import com.dxshulya.wasabi.model.User
import com.dxshulya.wasabi.paging.FavoritePagingSource
import com.dxshulya.wasabi.paging.TaskPagingSource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

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

    fun getTasks(pageConfig: PagingConfig = getDefaultPageConfig()): Flowable<PagingData<Task>> {
        return Pager(
            config = pageConfig,
            pagingSourceFactory = { TaskPagingSource(api) }
        ).flowable
    }

    fun getFavorites(pageConfig: PagingConfig = getDefaultPageConfig()): Flowable<PagingData<Favorites.Favorite>> {
        return Pager(
            config = pageConfig,
            pagingSourceFactory = {FavoritePagingSource(api)}
        ).flowable
    }

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(
            pageSize = 20,
            initialLoadSize = 10,
            enablePlaceholders = true
        )
    }

    fun postFavorite(favorite: Task): Observable<Authorization> {
        return api.postFavorite(favorite)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun deleteFavorite(id: Int): Observable<Authorization> {
        return api.deleteFavorite(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}