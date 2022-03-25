package com.dxshulya.wasabi.data

import com.dxshulya.wasabi.model.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface Api {
    //Auth
    @POST("auth/login")
    fun postLogin(
        @Body body: User
    ): Observable<Authorization>

    @POST("auth/registration")
    fun postRegistration(
        @Body body: User
    ): Observable<Authorization>

    //Tasks
    @GET("tasks/createArrayTasks")
    fun getTasks(
        @Query("page") page: Int,
        @Query("count") count: Int,
    ): Single<List<Task>>

    //Favorites
    @POST("favourites/addedFavourites")
    fun postFavorite(
        @Body body: Task
    ): Observable<Authorization>

    @GET("favourites/getAllFavourites")
    fun getFavorites(
        @Query ("page") page: Int,
        @Query ("count") count: Int,
    ): Single<Favorites>

    @DELETE("favourites/deleteFavourites")
    fun deleteFavorite(
        @Query ("id") id: String,
    ) : Observable<Authorization>

    @GET("favourites/getTotalCountFavourites")
    fun getTotalCountFavorites(): Observable<TotalCount>
}