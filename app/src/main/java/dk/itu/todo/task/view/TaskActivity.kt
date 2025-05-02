package dk.itu.todo.task.view


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import dk.itu.todo.R
import dk.itu.todo.model.Location
import dk.itu.todo.model.LocationRepository
import dk.itu.todo.model.database.DBCreate
import dk.itu.todo.task.viewmodel.TaskViewModel
import java.io.File


class TaskActivity : AppCompatActivity() {

    private lateinit var titleEt: EditText
    private lateinit var descEt: EditText
    private lateinit var prioEt: EditText
    private lateinit var takePicBtn: Button
    private lateinit var addBtn: Button
    private lateinit var imageView: ImageView
    private lateinit var spinner: Spinner
    private lateinit var addLocationBtn: Button

    private lateinit var viewModel: TaskViewModel

    private lateinit var dbHelper: DBCreate
    private var imagePath: String? = null
    private var photoUri: Uri? = null
    private lateinit var locationRepository: LocationRepository

    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>

    private val REQUEST_CODE_ADD_LOCATION = 1
    private val REQUEST_CAMERA_PERMISSION = 100

    private var selectedLocation: Location? = null
    private lateinit var locations: List<Location>
    private lateinit var locationNames: MutableList<String>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        titleEt    = findViewById(R.id.etTitle)
        descEt     = findViewById(R.id.etDescription)
        prioEt     = findViewById(R.id.etPriority)
        takePicBtn = findViewById(R.id.button_take_picture)
        addBtn     = findViewById(R.id.button_add_task)
        imageView  = findViewById(R.id.imageViewTask)
        spinner          = findViewById(R.id.spinner_choose_location)
        addLocationBtn   = findViewById(R.id.button_add_location)

        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        locationRepository = LocationRepository(this)

        setupLocationSpinner()
        setupTakePicture()

        addLocationBtn.setOnClickListener {
            startActivityForResult(
                Intent(this, AddLocationActivity::class.java),
                REQUEST_CODE_ADD_LOCATION
            )
        }

        takePicBtn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
                )
            } else {
                dispatchTakePictureIntent()
            }
        }

        addBtn.setOnClickListener {
            val title = titleEt.text.toString().trim()
            val desc  = descEt.text.toString().trim()
            val prio  = prioEt.text.toString().toIntOrNull() ?: 0

            viewModel.addTask(title, desc, prio, imagePath, selectedLocation)

            finish()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            dispatchTakePictureIntent()
        } else {
            Toast.makeText(
                this,
                "Camera permission is required to take pictures",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun dispatchTakePictureIntent() {
        val fileName   = "IMG_${System.currentTimeMillis()}"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: filesDir
        val photoFile  = File.createTempFile(fileName, ".jpg", storageDir).apply {
            imagePath = absolutePath
        }
        photoUri = FileProvider.getUriForFile(
            this,
            "$packageName.fileprovider",
            photoFile
        )
        takePictureLauncher.launch(photoUri!!)
    }

    private fun setupLocationSpinner() {
        locations = locationRepository.getAll()
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

    private fun setupTakePicture() {
        takePictureLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    val uri = photoUri ?: return@registerForActivityResult
                    imageView.setImageURI(uri)
                } else {
                    Toast.makeText(this, "Picture not taken", Toast.LENGTH_SHORT).show()
                }
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
