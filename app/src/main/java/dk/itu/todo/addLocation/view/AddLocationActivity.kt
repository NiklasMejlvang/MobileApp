package dk.itu.todo.addLocation.view

import dk.itu.todo.addLocation.viewmodel.AddLocationViewModel
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import dk.itu.todo.R

class AddLocationActivity : AppCompatActivity() {

    private lateinit var viewModel: AddLocationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location)

        viewModel = ViewModelProvider(this)[AddLocationViewModel::class.java]

        val nameInput = findViewById<EditText>(R.id.editTextLocationName)
        val addressInput = findViewById<EditText>(R.id.editTextLocationAddress)
        val saveButton = findViewById<Button>(R.id.buttonSaveLocation)

        saveButton.setOnClickListener {
            val name = nameInput.text.toString()
            val address = addressInput.text.toString()

            if (name.isBlank() || address.isBlank()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.addLocation(name, address)
        }

        viewModel.locationResult.observe(this) { result ->
            result.onSuccess { newLocation ->
                Toast.makeText(this, "Location added", Toast.LENGTH_SHORT).show()
                val resultIntent = Intent().apply {
                    putExtra("name", newLocation.name)
                    putExtra("latitude", newLocation.latitude)
                    putExtra("longitude", newLocation.longitude)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }.onFailure {
                Toast.makeText(this, it.message ?: "Could not find location", Toast.LENGTH_SHORT).show()
            }
        }
    }
}