package com.dxshulya.wasabi.data.model

data class Authorization(
    val statusCode: Int,
    val message: Any,
    val error: String,
    val token: String,
    val login: String
)