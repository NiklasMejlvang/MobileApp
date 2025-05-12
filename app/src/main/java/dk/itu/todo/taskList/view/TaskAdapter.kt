package dk.itu.todo.taskList.view

import dk.itu.todo.utils.getRotatedBitmapFromUri
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dk.itu.todo.databinding.ItemTaskBinding
import dk.itu.todo.model.Task
import dk.itu.todo.taskList.viewmodel.TaskListViewModel
import android.view.View
import android.graphics.BitmapFactory

class TaskAdapter(
    private val viewModel: TaskListViewModel
) : ListAdapter<Task, TaskAdapter.TaskListViewHolder>(TaskDiffCallback()) {

    private var onDeleteClick: ((Task) -> Unit)? = null
    private var onCompleteClick: ((Task) -> Unit)? = null

    inner class TaskListViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

    private var onItemClick: ((Task) -> Unit)? = null

    fun setOnItemClickListener(listener: (Task) -> Unit) {
        onItemClick = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskListViewHolder(binding)
    }


    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        val task = getItem(position)
        holder.binding.apply {
            root.setOnClickListener { onItemClick?.invoke(task) }
            tvTitle.text = task.title
            tvDescription.text = task.description
            tvPriority.text = task.priority.toString()

            cbDone.setOnCheckedChangeListener(null)
            cbDone.isChecked = task.isCompleted
            cbDone.setOnCheckedChangeListener { _, isChecked ->
                task.isCompleted = isChecked
                Toast.makeText(holder.itemView.context, "${task.title} is ${if (isChecked) "Completed" else "Not Completed"}", Toast.LENGTH_SHORT).show()
                viewModel.updateTaskCompletion(task)
            }

            if (!task.imagePath.isNullOrEmpty()) {
                imageViewTaskItem.visibility = View.VISIBLE


                val context = holder.itemView.context
                val uri = Uri.fromFile(java.io.File(task.imagePath))
                val bmp = getRotatedBitmapFromUri(context, uri)
                imageViewTaskItem.setImageBitmap(bmp)

            } else {
                imageViewTaskItem.visibility = View.GONE
            }
            btnDelete.setOnClickListener {
                onDeleteClick?.invoke(task)
            }
        }
    }

    fun setOnDeleteClickListener(listener: (Task) -> Unit) {
        onDeleteClick = listener
    }

    fun setOnCompleteClickListener(listener: (Task) -> Unit) {
        onCompleteClick = listener
    }
}