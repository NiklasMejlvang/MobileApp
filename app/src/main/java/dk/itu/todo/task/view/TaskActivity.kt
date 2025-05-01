package dk.itu.todo.task.view

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import dk.itu.todo.R
import dk.itu.todo.model.TaskDB
import java.io.File

class TaskActivity : AppCompatActivity() {

    private lateinit var titleEt: EditText
    private lateinit var descEt: EditText
    private lateinit var prioEt: EditText
    private lateinit var doneCb: CheckBox
    private lateinit var takePicBtn: Button
    private lateinit var addBtn: Button
    private lateinit var imageView: ImageView

    private lateinit var dbHelper: TaskDB
    private var imagePath: String? = null
    private var photoUri: Uri? = null

    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        // 1) Bind views
        titleEt    = findViewById(R.id.etTitle)
        descEt     = findViewById(R.id.etDescription)
        prioEt     = findViewById(R.id.etPriority)
        doneCb     = findViewById(R.id.cbCompleted)
        takePicBtn = findViewById(R.id.button_take_picture)
        addBtn     = findViewById(R.id.button_add_task)
        imageView  = findViewById(R.id.imageViewTask)

        // 2) Init DB helper
        dbHelper = TaskDB(this)

        // 3) Register the camera launcher
        takePictureLauncher = registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { success ->
            if (success) {
                // ==== Key change: Elvis operator here ====
                val uri = photoUri
                    ?: return@registerForActivityResult Toast
                        .makeText(this, "No photo URI!", Toast.LENGTH_SHORT)
                        .show()
                imageView.setImageURI(uri)
            } else {
                Toast.makeText(this, "Picture not taken", Toast.LENGTH_SHORT).show()
            }
        }

        // 4) “Take Picture” button logic
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

        // 5) “Save Task” button logic
        addBtn.setOnClickListener {
            val title = titleEt.text.toString().trim()
            val desc  = descEt.text.toString().trim()
            val prio  = prioEt.text.toString().toIntOrNull() ?: 0
            val done  = if (doneCb.isChecked) 1 else 0

            dbHelper.writableDatabase.execSQL(
                "INSERT INTO Tasks (Title, Description, Priority, IsCompleted, ImagePath) VALUES (?,?,?,?,?)",
                arrayOf(title, desc, prio, done, imagePath)
            )
            finish()
        }
    }

    // handle camera-permission result
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

    // create the file + URI and launch the new API
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
}
