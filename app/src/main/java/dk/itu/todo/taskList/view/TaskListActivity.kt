package dk.itu.todo.taskList.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.itu.todo.R
import dk.itu.todo.task.view.TaskActivity
import dk.itu.todo.taskList.viewmodel.TaskListViewModel

class TaskListActivity : AppCompatActivity() {
    private lateinit var rvTaskList: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var viewModel: TaskListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        rvTaskList  = findViewById(R.id.rv_task_list)
        taskAdapter = TaskAdapter(mutableListOf())
        rvTaskList.adapter       = taskAdapter
        rvTaskList.layoutManager = LinearLayoutManager(this)

        viewModel = ViewModelProvider(this)[TaskListViewModel::class.java]
        viewModel.tasks.observe(this) { tasks ->
            taskAdapter.setTasks(tasks)
        }

        findViewById<Button>(R.id.button_add_task).setOnClickListener {
            startActivity(Intent(this, TaskActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadTasks()
    }
}
