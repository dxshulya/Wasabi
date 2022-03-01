package com.dxshulya.wasabi.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.dxshulya.wasabi.model.Favorites

class FavoriteAdapter: ListAdapter<Favorites.Favorite, FavoriteViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Favorites.Favorite>() {
            override fun areItemsTheSame(oldItem: Favorites.Favorite, newItem: Favorites.Favorite): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Favorites.Favorite, newItem: Favorites.Favorite): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favoriteItem = getItem(position)
        holder.bind(favoriteItem)
    }
}