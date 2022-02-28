package com.dxshulya.wasabi.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dxshulya.wasabi.databinding.FavoriteFragmentBinding

class FavoriteFragment : Fragment() {

    class FavoriteFragment : Fragment() {

        companion object {
            fun newInstance() = FavoriteFragment()
        }

        private lateinit var viewModel: FavoriteViewModel
        //private val favoriteAdapter = FavoriteAdapter()

        private lateinit var binding: FavoriteFragmentBinding

        private lateinit var favoriteRecycler: RecyclerView

        private fun initUis() {
            favoriteRecycler = binding.favouriteRecycler
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FavoriteFragmentBinding.inflate(inflater, container, false)
            initUis()
//            favoriteRecycler.apply {
//                adapter = favoriteAdapter
//                layoutManager = LinearLayoutManager(context)
//                setHasFixedSize(true)
//            }
//            viewModel.favorites.observe(viewLifecycleOwner) {
//                favoriteAdapter.submitList(it)
//            }

            return binding.root
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]
        }
    }
}