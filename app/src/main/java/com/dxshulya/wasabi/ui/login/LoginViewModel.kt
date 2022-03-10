package com.dxshulya.wasabi.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.dxshulya.wasabi.App
import com.dxshulya.wasabi.datastore.SharedPreference
import com.dxshulya.wasabi.model.Authorization
import com.dxshulya.wasabi.model.User
import com.dxshulya.wasabi.repository.TaskRepository
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import retrofit2.HttpException
import javax.inject.Inject

@ExperimentalPagingApi
class LoginViewModel : ViewModel() {
    init {
        App.getInstance().appComponent.inject(this)
    }

    @Inject
    lateinit var taskRepository: TaskRepository

    @Inject
    lateinit var sharedPreference: SharedPreference

    private var _loginLiveData = MutableLiveData<Authorization>()
    val loginLiveData: LiveData<Authorization>
        get() = _loginLiveData

    fun postLogin() {
        val user = User(sharedPreference.email, sharedPreference.name, sharedPreference.password)
        taskRepository.loginUser(user)
            .subscribe({
                _loginLiveData.value = it
                sharedPreference.token = it.token
                sharedPreference.name = it.login
            }, {
                if (it is HttpException) {
                    val body = it.response()?.errorBody()
                    val gson = Gson()
                    val adapter: TypeAdapter<Authorization> =
                        gson.getAdapter(Authorization::class.java)
                    val error: Authorization = adapter.fromJson(body?.string())
                    _loginLiveData.value = error
                }
            })
    }

    fun updateEmail(email: String) {
        sharedPreference.email = email
    }

    fun updatePassword(password: String) {
        sharedPreference.password = password
    }
}