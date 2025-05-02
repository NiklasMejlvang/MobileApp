package dk.itu.todo.taskList.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.itu.todo.R
import dk.itu.todo.model.Task
import dk.itu.todo.task.view.TaskActivity
import dk.itu.todo.taskList.viewmodel.TaskListViewModel

class TaskListActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var adapter: TaskAdapter
    private lateinit var taskListViewModel: TaskListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        taskListViewModel = ViewModelProvider(this)[TaskListViewModel::class.java]

        rv = findViewById(R.id.rvTaskList)
        adapter = TaskAdapter(mutableListOf())
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)

        adapter.setOnDeleteClickListener { task ->
            taskListViewModel.deleteTask(task.title)
        }

        adapter.setOnCompleteClickListener { task ->
            taskListViewModel.updateTaskCompletion(task)
        }

        findViewById<Button>(R.id.button_add_task).setOnClickListener {
            startActivity(Intent(this, TaskActivity::class.java))
        }

        taskListViewModel.tasks.observe(this) { tasks ->
            val sortedTasks = tasks.sortedWith(
                compareBy<Task> { it.priority }.thenBy { it.isCompleted }
            )
            rv.post {
                adapter.setTasks(sortedTasks)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        taskListViewModel.loadTasks()
    }
}
