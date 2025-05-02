package dk.itu.todo.taskList.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.itu.todo.R
import dk.itu.todo.model.TaskDB
import dk.itu.todo.model.Task

class TaskListActivity : AppCompatActivity() {
    private lateinit var rv: RecyclerView
    private lateinit var adapter: TaskAdapter
    private lateinit var dbHelper: TaskDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        dbHelper = TaskDB(this)
        rv       = findViewById(R.id.rvTaskList)
        adapter  = TaskAdapter(mutableListOf())
        rv.adapter        = adapter
        rv.layoutManager  = LinearLayoutManager(this)

        findViewById<Button>(R.id.button_add_task).setOnClickListener {
            startActivity(Intent(this, dk.itu.todo.task.view.TaskActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // reload tasks from DB
        val list = mutableListOf<Task>()
        val db   = dbHelper.readableDatabase
        val c    = db.rawQuery(
            "SELECT Title, Description, Priority, IsCompleted, ImagePath FROM Tasks",
            null
        )
        while (c.moveToNext()) {
            list += Task(
                title       = c.getString(0),
                description = c.getString(1),
                priority    = c.getInt(2),
                isCompleted = c.getInt(3) == 1,
                imagePath   = c.getString(4)
            )
        }
        c.close()
        adapter.setTasks(list)
    }
}
