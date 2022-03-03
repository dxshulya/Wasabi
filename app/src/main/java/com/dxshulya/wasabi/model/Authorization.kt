package com.dxshulya.wasabi.model

data class Authorization(
    val statusCode: Int,
    val message: List<String>,
    val error: String,
    val token: String,
    val login: String
)