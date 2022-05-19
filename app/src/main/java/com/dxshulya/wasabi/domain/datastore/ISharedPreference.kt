package com.dxshulya.wasabi.domain.datastore

interface ISharedPreference {
    var email: String
    var name: String
    var password: String
    var token: String
    var isFirstRun: Boolean
    var totalCount: Int
    var isDarkMode: Boolean
}