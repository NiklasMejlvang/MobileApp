package dk.itu.todo.taskList.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.itu.todo.R
import dk.itu.todo.task.view.TaskActivity

class TaskListActivity : AppCompatActivity () {

    private lateinit var rvTaskList: RecyclerView
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        taskAdapter = TaskAdapter(mutableListOf())
        rvTaskList.adapter = taskAdapter
        rvTaskList.layoutManager = LinearLayoutManager(this)

        val buttonAddTask = findViewById<Button>(R.id.button_add_task)
        buttonAddTask.setOnClickListener {
            val intent = Intent(this, TaskActivity::class.java)
            startActivity(intent)
        }

    }
}