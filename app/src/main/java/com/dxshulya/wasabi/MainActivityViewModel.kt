package com.dxshulya.wasabi

import androidx.lifecycle.ViewModel
import com.dxshulya.wasabi.datastore.SharedPreference
import javax.inject.Inject

class MainActivityViewModel : ViewModel() {

    init {
        App.getInstance().appComponent.inject(this)
    }

    @Inject
    lateinit var sharedPreference: SharedPreference

    fun setNameOnHeader(): String {
        return sharedPreference.name
    }

    fun setEmailOnHeader(): String {
        return sharedPreference.email
    }

    fun isNightMode(): Boolean {
        return sharedPreference.isDarkMode
    }

    fun changeMode(isNight: Boolean) {
        sharedPreference.isDarkMode = isNight
    }

    fun setBadgeCount(): Int {
        return sharedPreference.totalCount
    }
}