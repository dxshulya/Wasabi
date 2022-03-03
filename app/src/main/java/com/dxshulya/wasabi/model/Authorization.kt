package com.dxshulya.wasabi.model

import com.google.gson.annotations.SerializedName

data class Authorization(
    val statusCode: Int,
    val message: Any,
//    @SerializedName("message")
//    val messages: List<Any>,
    val error: String,
    val token: String,
    val login: String
)