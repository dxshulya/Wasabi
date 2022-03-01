package com.dxshulya.wasabi.model

data class Favorites(
    val totalPage: Int = 1,
    val totalCount: Int = 10,
    val array: List<Favorite>
) {
    data class Favorite(
        val formula: String,
        val text: String,
        val answer: String
    )
}