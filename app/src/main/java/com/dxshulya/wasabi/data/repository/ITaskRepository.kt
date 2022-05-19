package com.dxshulya.wasabi.data.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dxshulya.wasabi.domain.model.*
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable

interface ITaskRepository {

    fun registrationUser(user: User): Observable<Authorization>
    fun loginUser(user: User): Observable<Authorization>
    fun getTasks(pageConfig: PagingConfig = getDefaultPageConfig()): Flowable<PagingData<Task>>
    fun getFavorites(pageConfig: PagingConfig = getDefaultPageConfig()): Flowable<PagingData<Favorites.Favorite>>
    fun postFavorite(favorite: Task): Observable<Authorization>
    fun deleteFavorite(id: String): Observable<Authorization>
    fun getTotalCount(): Observable<TotalCount>

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(
            pageSize = 20,
            initialLoadSize = 10,
            enablePlaceholders = false
        )
    }
}