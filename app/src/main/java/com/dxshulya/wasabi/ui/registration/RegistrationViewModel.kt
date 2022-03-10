package com.dxshulya.wasabi.ui.registration

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
class RegistrationViewModel : ViewModel() {

    init {
        App.getInstance().appComponent.inject(this)
    }

    @Inject
    lateinit var taskRepository: TaskRepository

    @Inject
    lateinit var sharedPreference: SharedPreference

    private var _registrationLiveData = MutableLiveData<Authorization>()
    val registrationLiveData: LiveData<Authorization>
        get() = _registrationLiveData

    fun postRegistration() {
        val user = User(
            sharedPreference.email,
            sharedPreference.name,
            sharedPreference.password
        )
        taskRepository.registrationUser(user)
            .subscribe({
                _registrationLiveData.value = it
                sharedPreference.token = it.token
            }, {
                if (it is HttpException) {
                    val body = it.response()?.errorBody()
                    val gson = Gson()
                    val adapter: TypeAdapter<Authorization> =
                        gson.getAdapter(Authorization::class.java)
                    val error: Authorization = adapter.fromJson(body?.string())
                    _registrationLiveData.value = error
                }
            })
    }

    fun updateEmail(email: String) {
        sharedPreference.email = email
    }

    fun updateName(name: String) {
        sharedPreference.name = name
    }

    fun updatePassword(password: String) {
        sharedPreference.password = password
    }
}