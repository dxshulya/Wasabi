package com.dxshulya.wasabi.model

data class Authorization(
    val statusCode: Int,
    val message: String,
    val token: String
)