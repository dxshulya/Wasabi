package com.dxshulya.wasabi.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dxshulya.wasabi.R
import com.dxshulya.wasabi.ui.adapter.TaskAdapter
import com.dxshulya.wasabi.databinding.TaskFragmentBinding
import com.google.android.material.progressindicator.CircularProgressIndicator

class TaskFragment : Fragment() {

    private lateinit var viewModel: TaskViewModel

    private lateinit var binding: TaskFragmentBinding

    private lateinit var taskRefresh: SwipeRefreshLayout
    private lateinit var taskRecycler: RecyclerView
    private lateinit var taskBar: CircularProgressIndicator

    private var taskAdapter: TaskAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TaskFragmentBinding.inflate(inflater, container, false)

        initUis()

        taskRecycler.apply {
            adapter = TaskAdapter(viewLifecycleOwner).also { taskAdapter = it }
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.tasks.observe(viewLifecycleOwner) {
            taskRefresh.isRefreshing = false
            taskAdapter?.submitData(viewLifecycleOwner.lifecycle, it)
        }

        taskRefresh.setOnRefreshListener {
            taskRefresh.isRefreshing = false
            viewModel.getTasks()
        }
        taskRefresh.setColorSchemeColors(resources.getColor(R.color.red))

        taskAdapter?.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                taskBar.visibility = View.VISIBLE
            }
            else {
                taskBar.visibility = View.GONE

                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    Toast.makeText(binding.root.context, it.error.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
    }

    private fun initUis() {
        taskRefresh = binding.taskRefresh
        taskRecycler = binding.taskRecycler
        taskBar = binding.taskBar
    }
}