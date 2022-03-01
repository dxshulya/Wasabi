package com.dxshulya.wasabi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dxshulya.wasabi.R
import com.dxshulya.wasabi.model.Task

class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val taskNumber: TextView = view.findViewById(R.id.task_number)
    private val taskFormula: TextView = view.findViewById(R.id.task_formula)
    private val taskText: TextView = view.findViewById(R.id.task_text)
    private val taskAnswer: TextView = view.findViewById(R.id.task_answer)

    fun bind(task: Task) {
        taskNumber.text = "№ " + (position + 1)
        taskFormula.text = task.formula
        taskText.text = task.text
        taskAnswer.text = "Ответ: " + task.answer
    }

    companion object {
        fun create(parent: ViewGroup): TaskViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.task_item, parent, false)
            return TaskViewHolder(view)
        }
    }
}