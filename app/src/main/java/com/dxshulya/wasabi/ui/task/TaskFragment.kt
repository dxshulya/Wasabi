package com.dxshulya.wasabi.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dxshulya.wasabi.R
import com.dxshulya.wasabi.adapter.TaskAdapter
import com.dxshulya.wasabi.databinding.TaskFragmentBinding
import com.google.android.material.navigation.NavigationBarItemView
import com.google.android.material.navigation.NavigationView

class TaskFragment : Fragment() {

    companion object {
        fun newInstance() = TaskFragment()
    }

    private lateinit var viewModel: TaskViewModel

    private lateinit var binding: TaskFragmentBinding

    private lateinit var taskRefresh: SwipeRefreshLayout
    private lateinit var taskRecycler: RecyclerView

    private var taskAdapter = TaskAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TaskFragmentBinding.inflate(inflater, container, false)

        initUis()

        taskRecycler.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(context)
        }
        viewModel.tasks.observe(viewLifecycleOwner) {
            taskRefresh.isRefreshing = false
            taskAdapter.submitList(it)
        }
        taskRefresh.setOnRefreshListener {
            taskRefresh.isRefreshing = false
            viewModel.getTasks()
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
    }
}