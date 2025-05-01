package dk.itu.todo.task.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 100
        private const val REQUEST_IMAGE_CAPTURE    = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        // 1) bind all views
        titleEt    = findViewById(R.id.etTitle)
        descEt     = findViewById(R.id.etDescription)
        prioEt     = findViewById(R.id.etPriority)
        doneCb     = findViewById(R.id.cbCompleted)
        takePicBtn = findViewById(R.id.button_take_picture)
        addBtn     = findViewById(R.id.button_add_task)
        imageView  = findViewById(R.id.imageViewTask)

        // 2) init DB helper
        dbHelper = TaskDB(this)

        // 3) camera‐button listener
        takePicBtn.setOnClickListener {
            // debug toast to confirm click
            Toast.makeText(this, "Take picture clicked", Toast.LENGTH_SHORT).show()

            // check runtime permission
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

        // 4) save‐task button
        addBtn.setOnClickListener {
            val title = titleEt.text.toString().trim()
            val desc  = descEt.text.toString().trim()
            val prio  = prioEt.text.toString().toIntOrNull() ?: 0
            val done  = if (doneCb.isChecked) 1 else 0

            // insert into SQLite
            val db = dbHelper.writableDatabase
            db.execSQL(
                "INSERT INTO Tasks (Title, Description, Priority, IsCompleted, ImagePath) VALUES (?,?,?,?,?)",
                arrayOf(title, desc, prio, done, imagePath)
            )

            finish()  // back to list
        }
    }

    // handle user’s response to the permission prompt
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                Toast.makeText(
                    this,
                    "Camera permission is required to take pictures",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // launch the camera app, saving to our own file
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(packageManager)?.let {
                val photoFile = createImageFile()
                val photoUri  = FileProvider.getUriForFile(
                    this,
                    "$packageName.fileprovider",
                    photoFile
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    // create a temp JPEG file in app-specific pictures dir
    private fun createImageFile(): File {
        val fileName   = "IMG_${System.currentTimeMillis()}"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            ?: filesDir
        return File.createTempFile(fileName, ".jpg", storageDir).apply {
            imagePath = absolutePath
        }
    }

    // receive the camera’s result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            imageView.setImageURI(Uri.fromFile(File(imagePath!!)))
        }
    }
}
