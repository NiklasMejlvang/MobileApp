package dk.itu.todo.taskList.view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dk.itu.todo.model.Task
import android.view.LayoutInflater
import dk.itu.todo.databinding.ItemTaskBinding

class TaskAdapter(private var tasks: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskListViewHolder>() {
    inner class TaskListViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        val task = tasks[position]
        holder.binding.apply {
            tvTitle.text = task.title
            tvDescription.text = task.description
            tvPriority.text = task.priority.toString()
            cbDone.isChecked = task.isCompleted
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}