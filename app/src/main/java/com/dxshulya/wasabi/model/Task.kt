package com.dxshulya.wasabi.model

data class Task(
    val id: String,
    val text: String,
    val answer: String,
    val formula: String,
    val isLiked: Boolean? = null
)