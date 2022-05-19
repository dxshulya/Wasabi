package com.dxshulya.wasabi.domain.model

data class Favorites(
    val totalPage: Int,
    val totalCount: Int,
    val array: List<Favorite>
) {
    data class Favorite(
        val id: String,
        val formula: String,
        val text: String,
        val answer: String,
        var isDisliked: Boolean = false
    )
}