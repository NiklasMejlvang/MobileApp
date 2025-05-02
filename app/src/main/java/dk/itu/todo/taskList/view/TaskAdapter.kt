package dk.itu.todo.taskList.view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dk.itu.todo.model.Task
import android.view.LayoutInflater
import dk.itu.todo.databinding.ItemTaskBinding

class TaskAdapter(private var tasks: MutableList<Task>) : RecyclerView.Adapter<TaskAdapter.TaskListViewHolder>() {

    inner class TaskListViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

    private var onDeleteClick: ((Task) -> Unit)? = null
    private var onCompleteClick: ((Task) -> Unit)? = null

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

            btnDelete.setOnClickListener {
                onDeleteClick?.invoke(task)
            }

            cbDone.setOnCheckedChangeListener { _, isChecked ->
                task.isCompleted = isChecked
                onCompleteClick?.invoke(task)
            }
        }
    }

    override fun getItemCount(): Int = tasks.size

    fun setTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        sortTasks()
        notifyDataSetChanged()
    }

    private fun sortTasks() {
        tasks.sortWith(compareBy<Task> { it.priority }.thenBy { it.isCompleted })
    }

    fun setOnDeleteClickListener(listener: (Task) -> Unit) {
        onDeleteClick = listener
    }

    fun setOnCompleteClickListener(listener: (Task) -> Unit) {
        onCompleteClick = listener
    }
}

