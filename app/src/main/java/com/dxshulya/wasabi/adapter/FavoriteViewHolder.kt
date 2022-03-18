package com.dxshulya.wasabi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dxshulya.wasabi.R
import com.dxshulya.wasabi.model.Favorites

class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val favoriteNumber: TextView = view.findViewById(R.id.favorite_number)
    private val favoriteFormula: TextView = view.findViewById(R.id.favorite_formula)
    private val favoriteText: TextView = view.findViewById(R.id.favorite_text)
    private val favoriteAnswer: TextView = view.findViewById(R.id.favorite_answer)
    private val favoriteLike: ImageView = view.findViewById(R.id.favorite_like)

    private var favorite: Favorites.Favorite? = null

    fun bind(favorite: Favorites.Favorite?) {
        if (favorite == null) {
            favoriteNumber.visibility = View.GONE
            favoriteFormula.visibility = View.GONE
            favoriteText.visibility = View.GONE
            favoriteAnswer.visibility = View.GONE
            favoriteLike.visibility = View.GONE
        } else showFavorites(favorite)

    }

    private fun showFavorites(favorite: Favorites.Favorite) {
        this.favorite = favorite
        favoriteNumber.text = "№ " + (position + 1)
        favoriteFormula.text = favorite.formula
        favoriteText.text = favorite.text
        favoriteAnswer.text = "Ответ: " + favorite.answer

        showFavoriteDislike(favorite)

        favoriteLike.setOnClickListener {
            favorite.isDisliked = true
            showFavoriteDislike(favorite)
        }
    }

    companion object {
        fun create(parent: ViewGroup): FavoriteViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.favorite_item, parent, false)
            return FavoriteViewHolder(view)
        }
    }

    private fun showFavoriteDislike(favorite: Favorites.Favorite) {
        if (favorite.isDisliked) {
            favoriteLike.setImageResource(R.drawable.ic_favorite_border)
        } else {
            favoriteLike.setImageResource(R.drawable.ic_favorite)
        }
    }

}