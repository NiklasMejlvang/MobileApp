package dk.itu.todo.taskListActivity

import androidx.recyclerview.widget.RecyclerView
import dk.itu.todo.model.Task

class TaskAdapter(var tasks: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskListViewHolder>() {
    inner class TaskListViewHolder(val binding: TaskBinding) : RecyclerView.ViewHolder(binding.root)
}