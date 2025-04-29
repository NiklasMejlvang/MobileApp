package dk.itu.todo.task.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dk.itu.todo.R
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import dk.itu.todo.model.Task
import dk.itu.todo.model.TaskRepository

class TaskActivity : AppCompatActivity() {

        private lateinit var titleEt: EditText
        private lateinit var descEt: EditText
        private lateinit var prioEt: EditText
        private lateinit var doneCb: CheckBox
        private lateinit var addBtn: Button
        private lateinit var repository: TaskRepository

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_task)

            titleEt = findViewById(R.id.etTitle)
            descEt  = findViewById(R.id.etDescription)
            prioEt  = findViewById(R.id.etPriority)
            doneCb  = findViewById(R.id.cbCompleted)
            addBtn  = findViewById(R.id.button_add_task)

            repository = TaskRepository(this)

            addBtn.setOnClickListener {
                val task = Task(
                    title = titleEt.text.toString().trim(),
                    description = descEt.text.toString().trim(),
                    priority = prioEt.text.toString().toIntOrNull() ?: 0,
                    isCompleted = doneCb.isChecked
                )
                repository.addTask(task)
                finish()
            }
        }
    }

