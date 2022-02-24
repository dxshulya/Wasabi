package com.dxshulya.wasabi.datastore

import android.content.Context

class SharedPreference(context: Context) {

    companion object {
        private const val EMAIL = "e-mail"
        private const val NAME = "name"
        private const val PASSWORD = "password"
        private const val JWT_TOKEN = "jwt_token"
        private const val IS_FIRST_RUN = "true"
    }

    private var sharedPreferences = context.getSharedPreferences("user-pref", Context.MODE_PRIVATE)
    private var sharedPreferencesEditor = sharedPreferences.edit()

    private fun setString(key: String, value: String) {
        sharedPreferencesEditor.putString(key, value).apply()
    }

    private fun getString(key: String, defaultValue: String = ""): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    private fun setBoolean(key: String, value: Boolean) {
        sharedPreferencesEditor.putBoolean(key, value).apply()
    }

    private fun getBoolean(key: String, defaultValue: Boolean = true): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    var email: String
        get() = getString(EMAIL)
        set(value) {
            setString(EMAIL, value)
        }
    var name: String
        get() = getString(NAME)
        set(value) {
            setString(NAME, value)
        }
    var password: String
        get() = getString(PASSWORD)
        set(value) {
            setString(PASSWORD, value)
        }
    var token: String
        get() = getString(JWT_TOKEN)
        set(value) {
            setString(JWT_TOKEN, value)
        }
    var isFirstRun: Boolean
        get() = getBoolean(IS_FIRST_RUN)
        set(value) {
            setBoolean(IS_FIRST_RUN, value)
        }
}