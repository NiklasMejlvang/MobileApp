package dk.itu.todo.taskList.view

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dk.itu.todo.databinding.ItemTaskBinding
import dk.itu.todo.model.Task
import java.io.File

class TaskAdapter(private val tasks: MutableList<Task>)
    : RecyclerView.Adapter<TaskAdapter.VH>() {

    inner class VH(val b: ItemTaskBinding)
        : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
    )

    override fun onBindViewHolder(holder: VH, pos: Int) {
        val t = tasks[pos]
        with(holder.b) {
            tvTitle.text       = t.title
            tvDescription.text = t.description
            tvPriority.text    = t.priority.toString()
            cbDone.isChecked   = t.isCompleted

            if (!t.imagePath.isNullOrEmpty()) {
                imageViewTaskItem.visibility = View.VISIBLE
                imageViewTaskItem.setImageURI(
                    Uri.fromFile(File(t.imagePath))
                )
            } else {
                imageViewTaskItem.visibility = View.GONE
            }
        }
    }

    override fun getItemCount() = tasks.size

    fun setTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }
}
