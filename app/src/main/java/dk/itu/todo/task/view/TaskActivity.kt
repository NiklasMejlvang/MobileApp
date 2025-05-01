package dk.itu.todo.task.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import dk.itu.todo.R
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import dk.itu.todo.task.viewmodel.TaskViewModel


import dk.itu.todo.model.Location
import dk.itu.todo.model.LocationRepository
import dk.itu.todo.model.task.view.AddLocationActivity


class TaskActivity : AppCompatActivity() {

    private lateinit var titleEt: EditText
    private lateinit var descEt: EditText
    private lateinit var prioEt: EditText
    private lateinit var doneCb: CheckBox
    private lateinit var addBtn: Button
    private lateinit var viewModel: TaskViewModel

    // NEW:
    private lateinit var spinner: Spinner
    private lateinit var addLocationBtn: Button
    private var selectedLocation: Location? = null
    private lateinit var locations: List<Location>
    private lateinit var locationNames: MutableList<String>
    private val REQUEST_CODE_ADD_LOCATION = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        titleEt = findViewById(R.id.etTitle)
        descEt  = findViewById(R.id.etDescription)
        prioEt  = findViewById(R.id.etPriority)
        doneCb  = findViewById(R.id.cbCompleted)
        addBtn  = findViewById(R.id.button_add_task)

        spinner          = findViewById(R.id.spinner_choose_location)
        addLocationBtn   = findViewById(R.id.button_add_location)

        setupLocationSpinner()
        addLocationBtn.setOnClickListener {
            startActivityForResult(
                Intent(this, AddLocationActivity::class.java),
                REQUEST_CODE_ADD_LOCATION
            )
        }

        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        addBtn.setOnClickListener {
            val title = titleEt.text.toString().trim()
            val desc = descEt.text.toString().trim()
            val prio = prioEt.text.toString().toIntOrNull() ?: 0
            val done = doneCb.isChecked

            viewModel.addTask(title, desc, prio, done, selectedLocation)
            finish()
        }
    }

    private fun setupLocationSpinner() {
        locations = LocationRepository.getAll()
        locationNames = locations.map { it.name }.toMutableList()

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, locationNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                selectedLocation = locations[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) { }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_LOCATION && resultCode == Activity.RESULT_OK) {
            // refresh spinner, auto-select the new one
            setupLocationSpinner()
            spinner.setSelection(locationNames.size - 1)
        }
    }
}
