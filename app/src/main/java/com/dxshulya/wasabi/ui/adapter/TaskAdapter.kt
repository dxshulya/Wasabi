package com.dxshulya.wasabi.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dxshulya.wasabi.R
import com.dxshulya.wasabi.databinding.TaskItemBinding
import com.dxshulya.wasabi.domain.model.Task
import com.dxshulya.wasabi.ui.item.TaskItemViewModel


class TaskAdapter(private val fragmentLifecycleOwner: LifecycleOwner) :
    PagingDataAdapter<Task, TaskAdapter.TaskViewHolder>(COMPARATOR) {

    var isPressed = false

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding =
            TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
                this.lifecycleOwner = fragmentLifecycleOwner
            }
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class TaskViewHolder(private val binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(task: Task) {
            binding.viewModel = TaskItemViewModel(task).apply {
                showLikes(task)
                this.favoriteLiveData.observe(fragmentLifecycleOwner) {
                    showLikes(task)
                    getTotalCount()
                }
            }
            with(binding) {
                showAnswer(task)
                taskNumber.text = "№ " + (position + 1)
                taskFormula.text = task.formula
                taskText.text = task.text
                taskAnswer.setOnClickListener {
                    isPressed = !isPressed
                    task.isShowAnswer = isPressed
                    showAnswer(task)
                }
            }
        }

        private fun showLikes(task: Task) {
            if (task.isLiked) {
                binding.taskLike.setImageResource(R.drawable.ic_favorite)
            } else {
                binding.taskLike.setImageResource(R.drawable.ic_favorite_border)
            }
        }

        @SuppressLint("SetTextI18n")
        private fun showAnswer(task: Task) {
            if (task.isShowAnswer) {
                binding.taskAnswer.text = "Ответ: " + task.answer
            } else binding.taskAnswer.text = "Показать ответ"
        }
    }
}