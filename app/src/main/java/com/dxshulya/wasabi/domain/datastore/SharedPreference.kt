package com.dxshulya.wasabi.domain.datastore

import android.content.Context

class SharedPreference(context: Context): ISharedPreference {

    companion object {
        private const val EMAIL = "e-mail"
        private const val NAME = "name"
        private const val PASSWORD = "password"
        private const val JWT_TOKEN = "jwt_token"
        private const val IS_FIRST_RUN = "true"
        private const val TOTAL_COUNT = "count"
        private const val IS_DARK_MODE = "false"
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

    private fun getBooleanTheme(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    private fun setInt(key: String, value: Int) {
        sharedPreferencesEditor.putInt(key, value).apply()
    }

    private fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    override var email: String
        get() = getString(EMAIL)
        set(value) {
            setString(EMAIL, value)
        }
    override var name: String
        get() = getString(NAME)
        set(value) {
            setString(NAME, value)
        }
    override var password: String
        get() = getString(PASSWORD)
        set(value) {
            setString(PASSWORD, value)
        }
    override var token: String
        get() = getString(JWT_TOKEN)
        set(value) {
            setString(JWT_TOKEN, value)
        }
    override var isFirstRun: Boolean
        get() = getBoolean(IS_FIRST_RUN)
        set(value) {
            setBoolean(IS_FIRST_RUN, value)
        }
    override var totalCount: Int
        get() = getInt(TOTAL_COUNT)
        set(value) {
            setInt(TOTAL_COUNT, value)
        }
    override var isDarkMode: Boolean
        get() = getBooleanTheme(IS_DARK_MODE)
        set(value) {
            setBoolean(IS_DARK_MODE, value)
        }
}