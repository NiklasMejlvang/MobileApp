package dk.itu.todo.task.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dk.itu.todo.R
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import dk.itu.todo.model.TaskDB

    class TaskActivity : AppCompatActivity() {

        private lateinit var titleEt: EditText
        private lateinit var descEt: EditText
        private lateinit var prioEt: EditText
        private lateinit var doneCb: CheckBox
        private lateinit var addBtn: Button
        private lateinit var dbHelper: TaskDB

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_task)

            // 1) bind views
            titleEt = findViewById(R.id.editTextTitle)
            descEt  = findViewById(R.id.editTextDescription)
            prioEt  = findViewById(R.id.editTextPriority)
            doneCb  = findViewById(R.id.checkBoxCompleted)
            addBtn  = findViewById(R.id.button_add_task)

            // 2) init DB helper
            dbHelper = TaskDB(this)

            // 3) insert on click
            addBtn.setOnClickListener {
                val title = titleEt.text.toString().trim()
                val desc  = descEt.text.toString().trim()
                val prio  = prioEt.text.toString().toIntOrNull() ?: 0
                val done  = if (doneCb.isChecked) 1 else 0

                val db = dbHelper.writableDatabase
                db.execSQL(
                    "INSERT INTO Tasks (Title, Description, Priority, IsCompleted) VALUES (?,?,?,?)",
                    arrayOf(title, desc, prio, done)
                )

                // close this screen
                finish()
            }
        }
    }

