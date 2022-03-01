package com.dxshulya.wasabi.model

data class Favorites(
    val totalPage: Int,
    val totalCount: Int,
    val array: List<Favorite>
) {
    data class Favorite(
        val formula: String,
        val text: String,
        val answer: String
    )
}