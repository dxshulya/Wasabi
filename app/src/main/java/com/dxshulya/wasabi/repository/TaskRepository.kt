package com.dxshulya.wasabi.repository

import com.dxshulya.wasabi.data.Api
import com.dxshulya.wasabi.model.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
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

    fun getTasks(token: String, count: Int, page: Int): Observable<List<Task>> {
        return api.getTasks(token, count, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun postFavorite(token: String, favorite: Task): Observable<Authorization> {
        return api.postFavorite(token, favorite)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getFavorites(token: String, count: Int, page: Int): Observable<Favorites> {
        return api.getFavorites(token, count, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}