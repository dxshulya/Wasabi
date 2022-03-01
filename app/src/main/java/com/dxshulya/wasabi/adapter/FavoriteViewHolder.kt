package com.dxshulya.wasabi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dxshulya.wasabi.R
import com.dxshulya.wasabi.model.Favorites

class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val favoriteNumber: TextView = view.findViewById(R.id.favorite_number)
    private val favoriteFormula: TextView = view.findViewById(R.id.favorite_formula)
    private val favoriteText: TextView = view.findViewById(R.id.favorite_text)
    private val favoriteAnswer: TextView = view.findViewById(R.id.favorite_answer)

    fun bind(favorite: Favorites.Favorite) {
        favoriteNumber.text = "№ " + (position + 1)
        favoriteFormula.text = favorite.formula
        favoriteText.text = favorite.text
        favoriteAnswer.text = "Ответ: " + favorite.answer
    }

    companion object {
        fun create(parent: ViewGroup): FavoriteViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.favorite_item, parent, false)
            return FavoriteViewHolder(view)
        }
    }

}