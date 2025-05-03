package dk.itu.todo.taskList.view


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.itu.todo.R
import dk.itu.todo.map.view.MapActivity
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
        adapter = TaskAdapter(taskListViewModel)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)

        adapter.setOnItemClickListener { task ->
                val intent = Intent(this, TaskActivity::class.java).apply {
                        putExtra(TaskActivity.EXTRA_TASK_ID, task.id)
                   }
                startActivity(intent)
        }
        adapter.setOnDeleteClickListener { task ->
            taskListViewModel.deleteTask(task.id)
        }

        adapter.setOnCompleteClickListener { task ->
            taskListViewModel.updateTaskCompletion(task)
        }

        findViewById<Button>(R.id.button_add_task).setOnClickListener {
            startActivity(Intent(this, TaskActivity::class.java))
        }

        findViewById<Button>(R.id.button_map).setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }

        taskListViewModel.tasks.observe(this) { tasks ->
            adapter.submitList(tasks)
        }
    }

    override fun onResume() {
        super.onResume()
        taskListViewModel.loadTasks()
    }
}
