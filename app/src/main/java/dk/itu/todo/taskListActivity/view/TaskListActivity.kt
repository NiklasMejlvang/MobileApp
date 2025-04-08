package dk.itu.todo.taskListActivity.view

import android.os.Bundle
import android.widget.Adapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import dk.itu.todo.R
import dk.itu.todo.taskListActivity.TaskAdapter

class TaskListActivity : AppCompatActivity () {

    private lateinit var rvTaskList: RecyclerView
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        rvTaskList = findViewById(R.id.rv_task_list)
        taskAdapter = TaskAdapter(emptyList())
        rvTaskList.adapter(taskAdapter)
    }
}