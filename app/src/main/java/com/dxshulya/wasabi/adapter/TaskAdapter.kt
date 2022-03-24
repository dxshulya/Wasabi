package com.dxshulya.wasabi.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dxshulya.wasabi.R
import com.dxshulya.wasabi.databinding.TaskItemBinding
import com.dxshulya.wasabi.model.Task

class TaskAdapter : PagingDataAdapter<Task, TaskAdapter.TaskViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            showLikes(task)
            with(binding) {
                taskNumber.text = "№ " + (position + 1)
                taskFormula.text = task.formula
                taskText.text = task.text
                taskAnswer.text = "Ответ: " + task.answer
                with(taskLike) {
                    setOnClickListener {
                        task.isLiked = true
                        showLikes(task)
                    }
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
    }
}