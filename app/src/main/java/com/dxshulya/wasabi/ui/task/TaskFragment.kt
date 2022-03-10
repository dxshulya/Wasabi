package com.dxshulya.wasabi.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dxshulya.wasabi.R
import com.dxshulya.wasabi.adapter.TaskAdapter
import com.dxshulya.wasabi.databinding.TaskFragmentBinding
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class TaskFragment : Fragment() {

    companion object {
        fun newInstance() = TaskFragment()
    }

    private lateinit var viewModel: TaskViewModel

    private lateinit var binding: TaskFragmentBinding

    private lateinit var taskRefresh: SwipeRefreshLayout
    private lateinit var taskRecycler: RecyclerView
    private lateinit var taskBar: CircularProgressIndicator

    private var taskAdapter = TaskAdapter()

    private fun fetchTasks() {
        taskRefresh.isRefreshing = false
        taskBar.visibility = View.GONE
        lifecycleScope.launch {
            viewModel.getTasks().distinctUntilChanged().collectLatest {
                taskAdapter.submitData(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TaskFragmentBinding.inflate(inflater, container, false)

        initUis()
        taskBar.visibility = View.VISIBLE

        taskRecycler.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(context)
        }

        fetchTasks()
        taskRefresh.setOnRefreshListener {
            fetchTasks()
        }
        taskRefresh.setColorSchemeColors(resources.getColor(R.color.red))
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