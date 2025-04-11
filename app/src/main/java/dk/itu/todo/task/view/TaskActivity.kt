package dk.itu.todo.task.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dk.itu.todo.R

class TaskActivity : AppCompatActivity () {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

    }
}