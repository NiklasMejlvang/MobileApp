package dk.itu.todo.task.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dk.itu.todo.R
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import dk.itu.todo.task.viewmodel.TaskViewModel

class TaskActivity : AppCompatActivity() {

    private lateinit var titleEt: EditText
    private lateinit var descEt: EditText
    private lateinit var prioEt: EditText
    private lateinit var doneCb: CheckBox
    private lateinit var addBtn: Button
    private lateinit var viewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        titleEt = findViewById(R.id.etTitle)
        descEt  = findViewById(R.id.etDescription)
        prioEt  = findViewById(R.id.etPriority)
        doneCb  = findViewById(R.id.cbCompleted)
        addBtn  = findViewById(R.id.button_add_task)

        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        addBtn.setOnClickListener {
            val title = titleEt.text.toString().trim()
            val desc = descEt.text.toString().trim()
            val prio = prioEt.text.toString().toIntOrNull() ?: 0
            val done = doneCb.isChecked

            viewModel.addTask(title, desc, prio, done)
            finish()
        }
    }
}
