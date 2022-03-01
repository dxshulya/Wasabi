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
    fun getTasks(
        @Header("Authorization") token: String,
        @Query("count") count: Int,
        @Query("page") page: Int,
    ): Observable<List<Task>>

    //Favorites
    @POST("favourites/addedFavourites")
    fun postFavorite(
        @Header("Authorization") token: String,
        @Body body: Task
    ): Observable<Authorization>

    @GET("favourites/getAllFavourites")
    fun getFavorites(
        @Header("Authorization") token: String,
        @Query ("count") count: Int,
        @Query ("page") page: Int,
    ): Observable<Favorites>
}