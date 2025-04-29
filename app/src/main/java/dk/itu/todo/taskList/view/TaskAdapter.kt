package dk.itu.todo.taskList.view

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dk.itu.todo.databinding.ItemTaskBinding
import dk.itu.todo.model.Task
import java.io.File

class TaskAdapter(private var tasks: MutableList<Task>)
    : RecyclerView.Adapter<TaskAdapter.TaskListViewHolder>() {

    inner class TaskListViewHolder(val binding: ItemTaskBinding)
        : RecyclerView.ViewHolder(binding.root)

    var onDeleteClick: ((Task) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = TaskListViewHolder(
        ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
    )

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        val task = tasks[position]
        with(holder.binding) {
            tvTitle.text       = task.title
            tvDescription.text = task.description
            tvPriority.text    = task.priority.toString()
            cbDone.isChecked   = task.isCompleted

            if (!task.imagePath.isNullOrEmpty()) {
                imageViewTaskItem.visibility = View.VISIBLE
                imageViewTaskItem.setImageURI(
                    Uri.fromFile(File(task.imagePath))
                )
            } else {
                imageViewTaskItem.visibility = View.GONE
            }

            btnDelete.setOnClickListener {
                onDeleteClick?.invoke(task)
            }
        }
    }

    override fun getItemCount() = tasks.size

    fun setTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    fun deleteTask(task: Task) {
        val pos = tasks.indexOf(task)
        if (pos >= 0) {
            tasks.removeAt(pos)
            notifyItemRemoved(pos)
        }
    }
}
