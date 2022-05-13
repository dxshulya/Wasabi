package com.dxshulya.wasabi.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dxshulya.wasabi.R
import com.dxshulya.wasabi.databinding.FavoriteItemBinding
import com.dxshulya.wasabi.data.model.Favorites
import com.dxshulya.wasabi.ui.item.FavoriteItemViewModel

class FavoriteAdapter(private val fragmentLifecycleOwner: LifecycleOwner) :
    PagingDataAdapter<Favorites.Favorite, FavoriteAdapter.FavoriteViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Favorites.Favorite>() {
            override fun areItemsTheSame(
                oldItem: Favorites.Favorite,
                newItem: Favorites.Favorite
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Favorites.Favorite,
                newItem: Favorites.Favorite
            ): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding =
            FavoriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
                this.lifecycleOwner = fragmentLifecycleOwner
            }
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favoriteItem = getItem(position)
        if (favoriteItem != null) {
            holder.bind(favoriteItem)
        }
    }

    inner class FavoriteViewHolder(private val binding: FavoriteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(favorite: Favorites.Favorite) {
            binding.viewModel = FavoriteItemViewModel(favorite).apply {
                showDislikes(favorite)
                this.deleteFavoriteLiveData.observe(fragmentLifecycleOwner) {
                    showDislikes(favorite)
                    getTotalCount()
                }
            }
            with(binding) {
                favoriteFormula.text = favorite.formula
                favoriteText.text = favorite.text
                favoriteAnswer.text = "Ответ: " + favorite.answer
                if (itemCount == 0) {

                }
            }
        }

        private fun showDislikes(favorite: Favorites.Favorite) {
            if (favorite.isDisliked) {
                binding.favoriteLike.setImageResource(R.drawable.ic_favorite_border)
            } else binding.favoriteLike.setImageResource(R.drawable.ic_favorite)
        }
    }
}