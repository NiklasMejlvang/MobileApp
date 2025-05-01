package dk.itu.todo.model.task.view

import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dk.itu.todo.model.Location
import dk.itu.todo.model.LocationRepository
import dk.itu.todo.R
import java.util.Locale

class AddLocationActivity : AppCompatActivity() {

    //New class for location functionality

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location)

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

            val geocoder = Geocoder(this, Locale.getDefault())
            val results = geocoder.getFromLocationName(address, 1)

            if (!results.isNullOrEmpty()) {
                val location = results[0]
                val latitude = location.latitude
                val longitude = location.longitude

                val newLocation = Location(name, latitude, longitude)
                LocationRepository.addLocation(newLocation)

                val resultIntent = Intent().apply {
                    putExtra("name", newLocation.name)
                    putExtra("latitude", newLocation.latitude)
                    putExtra("longitude", newLocation.longitude)
                }

                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Could not find location", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
