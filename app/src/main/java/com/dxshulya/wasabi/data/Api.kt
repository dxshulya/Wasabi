package com.dxshulya.wasabi.data

import com.dxshulya.wasabi.model.Authorization
import com.dxshulya.wasabi.model.Favorites
import com.dxshulya.wasabi.model.Task
import com.dxshulya.wasabi.model.User
import io.reactivex.rxjava3.core.Observable
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
    suspend fun getTasks(
        @Query("page") page: Int,
        @Query("count") count: Int,
    ): List<Task>

    //Favorites
    @POST("favourites/addedFavourites")
    fun postFavorite(
        @Body body: Task
    ): Observable<Authorization>

    @GET("favourites/getAllFavourites")
    fun getFavorites(
        @Query ("page") page: Int,
        @Query ("count") count: Int,
    ): Observable<Favorites>

    @DELETE("favourites/deleteFavourites")
    fun deleteFavorite(
        @Query ("id") id: Int,
    ) : Observable<Authorization>
}