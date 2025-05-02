package dk.itu.todo.taskList.view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dk.itu.todo.model.Task
import android.view.LayoutInflater
import dk.itu.todo.databinding.ItemTaskBinding
import android.view.View
import android.graphics.BitmapFactory

class TaskAdapter(private var tasks: MutableList<Task>) : RecyclerView.Adapter<TaskAdapter.TaskListViewHolder>() {

    inner class TaskListViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

    private var onDeleteClick: ((Task) -> Unit)? = null

    private var onItemClick: ((Task) -> Unit)? = null

    fun setOnItemClickListener(listener: (Task) -> Unit) {
        onItemClick = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskListViewHolder(binding)
    }


    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        val task = tasks[position]
        holder.binding.apply {
            root.setOnClickListener { onItemClick?.invoke(task) }
            tvTitle.text = task.title
            tvDescription.text = task.description
            tvPriority.text = task.priority.toString()
            cbDone.isChecked = task.isCompleted

            if (!task.imagePath.isNullOrEmpty()) {
                imageViewTaskItem.visibility = View.VISIBLE


                val bmp = BitmapFactory.decodeFile(task.imagePath)
                imageViewTaskItem.setImageBitmap(bmp)

            } else {
                imageViewTaskItem.visibility = View.GONE
            }
            btnDelete.setOnClickListener {
                onDeleteClick?.invoke(task)
            }
        }
    }

    override fun getItemCount(): Int = tasks.size

    fun setTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    fun setOnDeleteClickListener(listener: (Task) -> Unit) {
        onDeleteClick = listener
    }
}
