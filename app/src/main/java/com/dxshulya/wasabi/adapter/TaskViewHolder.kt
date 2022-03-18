package com.dxshulya.wasabi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dxshulya.wasabi.R
import com.dxshulya.wasabi.model.Task

class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val taskNumber: TextView = view.findViewById(R.id.task_number)
    private val taskFormula: TextView = view.findViewById(R.id.task_formula)
    private val taskText: TextView = view.findViewById(R.id.task_text)
    private val taskAnswer: TextView = view.findViewById(R.id.task_answer)
    private val taskLike: ImageView = view.findViewById(R.id.task_like)

    private var task: Task? = null

    fun bind(task: Task?) {
        if (task == null) {
            taskNumber.visibility = View.GONE
            taskFormula.visibility = View.GONE
            taskText.visibility = View.GONE
            taskAnswer.visibility = View.GONE
            taskLike.visibility = View.GONE
        } else {
            showTasks(task)
        }
    }

    private fun showTasks(task: Task) {
        this.task = task
        taskNumber.text = "№ " + (position + 1)
        taskFormula.text = task.formula
        taskText.text = task.text
        taskAnswer.text = "Ответ: " + task.answer

        showTaskLike(task)

        taskLike.setOnClickListener {
            task.isLiked = true
            showTaskLike(task)
        }
    }

    companion object {
        fun create(parent: ViewGroup): TaskViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.task_item, parent, false)
            return TaskViewHolder(view)
        }
    }

    private fun showTaskLike(task: Task) {
        if (task.isLiked) {
            taskLike.setImageResource(R.drawable.ic_favorite)
        } else {
            taskLike.setImageResource(R.drawable.ic_favorite_border)
        }
    }
}