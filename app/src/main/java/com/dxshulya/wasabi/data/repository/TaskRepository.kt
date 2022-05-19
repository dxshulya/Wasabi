package com.dxshulya.wasabi.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.dxshulya.wasabi.data.Api
import com.dxshulya.wasabi.data.paging.FavoritePagingSource
import com.dxshulya.wasabi.data.paging.TaskPagingSource
import com.dxshulya.wasabi.domain.model.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class TaskRepository(private val api: Api): ITaskRepository {

    override fun registrationUser(user: User): Observable<Authorization> {
        return api.postRegistration(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun loginUser(user: User): Observable<Authorization> {
        return api.postLogin(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getTasks(pageConfig: PagingConfig): Flowable<PagingData<Task>> {
        return Pager(
            config = pageConfig,
            pagingSourceFactory = { TaskPagingSource(api) }
        ).flowable
    }

    override fun getFavorites(pageConfig: PagingConfig): Flowable<PagingData<Favorites.Favorite>> {
        return Pager(
            config = pageConfig,
            pagingSourceFactory = { FavoritePagingSource(api) }
        ).flowable
    }



    override fun postFavorite(favorite: Task): Observable<Authorization> {
        return api.postFavorite(favorite)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun deleteFavorite(id: String): Observable<Authorization> {
        return api.deleteFavorite(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getTotalCount(): Observable<TotalCount> {
        return api.getTotalCountFavorites()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}