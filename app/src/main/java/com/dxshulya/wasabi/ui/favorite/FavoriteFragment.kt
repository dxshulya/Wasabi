package com.dxshulya.wasabi.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dxshulya.wasabi.R
import com.dxshulya.wasabi.adapter.FavoriteAdapter
import com.dxshulya.wasabi.databinding.FavoriteFragmentBinding
import com.google.android.material.progressindicator.CircularProgressIndicator

class FavoriteFragment : Fragment() {

    private lateinit var viewModel: FavoriteViewModel
    private var favoriteAdapter: FavoriteAdapter? = null

    private lateinit var binding: FavoriteFragmentBinding

    private lateinit var favoriteRecycler: RecyclerView
    private lateinit var favoriteRefresh: SwipeRefreshLayout
    private lateinit var emptyView: TextView
    private lateinit var emptyText: TextView
    private lateinit var favoriteBar: CircularProgressIndicator

    private fun initUis() {
        favoriteRecycler = binding.favouriteRecycler
        favoriteRefresh = binding.favoriteRefresh
        emptyView = binding.emptyView
        emptyText = binding.emptyText
        favoriteBar = binding.favoriteBar
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FavoriteFragmentBinding.inflate(inflater, container, false)
        initUis()
        favoriteRecycler.apply {
            adapter = FavoriteAdapter(viewLifecycleOwner).also { favoriteAdapter = it }
            layoutManager = LinearLayoutManager(context)
        }
        viewModel.favorites.observe(viewLifecycleOwner) {
            favoriteAdapter?.submitData(viewLifecycleOwner.lifecycle, it)
        }

        favoriteRefresh.setOnRefreshListener {
            favoriteRefresh.isRefreshing = false
            favoriteRecycler.adapter = FavoriteAdapter(viewLifecycleOwner).also { favoriteAdapter = it }
            viewModel.getFavorites()
        }

        favoriteRefresh.setColorSchemeColors(resources.getColor((R.color.green)))

        favoriteAdapter?.addLoadStateListener {
            if (it.append is LoadState.NotLoading && it.append.endOfPaginationReached) {
                emptyView.isVisible = favoriteAdapter?.itemCount!! < 1
                emptyText.isVisible = favoriteAdapter?.itemCount!! < 1
            }

            if (it.refresh is LoadState.Loading) {
                favoriteBar.visibility = View.VISIBLE
            }
            else {
                favoriteBar.visibility = View.GONE

                val errorState = when {
                    it.append is LoadState.Error -> it.append as LoadState.Error
                    it.prepend is LoadState.Error -> it.prepend as LoadState.Error
                    else -> null
                }
                errorState?.let { e ->
                    Toast.makeText(binding.root.context, e.error.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]
    }
}