package com.dxshulya.wasabi.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

class FavoriteFragment : Fragment() {

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    private lateinit var viewModel: FavoriteViewModel
    private var favoriteAdapter: FavoriteAdapter? = null

    private lateinit var binding: FavoriteFragmentBinding

    private lateinit var favoriteRecycler: RecyclerView
    private lateinit var favoriteRefresh: SwipeRefreshLayout
    private lateinit var emptyView: TextView
    private lateinit var emptyText: TextView

    private fun initUis() {
        favoriteRecycler = binding.favouriteRecycler
        favoriteRefresh = binding.favoriteRefresh
        emptyView = binding.emptyView
        emptyText = binding.emptyText
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
            viewModel.getFavorites()
        }

        favoriteRefresh.setColorSchemeColors(resources.getColor((R.color.green)))

        favoriteAdapter?.addLoadStateListener {
            if (it.append is LoadState.NotLoading && it.append.endOfPaginationReached) {
                emptyView.isVisible = favoriteAdapter?.itemCount!! < 1
                emptyText.isVisible = favoriteAdapter?.itemCount!! < 1
            }
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]
    }
}