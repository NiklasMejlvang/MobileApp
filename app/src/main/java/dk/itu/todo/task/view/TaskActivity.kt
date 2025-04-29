package dk.itu.todo.task.view

import android.Manifest
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
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import dk.itu.todo.R
import dk.itu.todo.task.viewmodel.TaskViewModel
import java.io.File

class TaskActivity : AppCompatActivity() {
    private lateinit var titleEt: EditText
    private lateinit var descEt: EditText
    private lateinit var prioEt: EditText
    private lateinit var doneCb: CheckBox
    private lateinit var addBtn: Button
    private lateinit var imageView: ImageView
    private lateinit var takePictureBtn: Button
    private lateinit var viewModel: TaskViewModel
    private var imagePath: String? = null

    companion object {
        private const val REQUEST_IMAGE_CAPTURE   = 1
        private const val REQUEST_CAMERA_PERMISSION = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        // Bind views
        titleEt        = findViewById(R.id.etTitle)
        descEt         = findViewById(R.id.etDescription)
        prioEt         = findViewById(R.id.etPriority)
        doneCb         = findViewById(R.id.cbCompleted)
        imageView      = findViewById(R.id.imageViewTask)
        takePictureBtn = findViewById(R.id.button_take_picture)
        addBtn         = findViewById(R.id.button_add_task)

        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        takePictureBtn.setOnClickListener {
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
            viewModel.addTask(
                titleEt.text.toString().trim(),
                descEt.text.toString().trim(),
                prioEt.text.toString().toIntOrNull() ?: 0,
                doneCb.isChecked,
                imagePath
            )
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            dispatchTakePictureIntent()
        } else {
            Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(packageManager)?.let {
                val photoFile = createImageFile()
                val photoURI  = FileProvider.getUriForFile(
                    this,
                    "$packageName.fileprovider",
                    photoFile
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun createImageFile(): File {
        val fileName   = "IMG_${System.currentTimeMillis()}"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            ?: filesDir
        return File.createTempFile(fileName, ".jpg", storageDir).apply {
            imagePath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageView.setImageURI(Uri.fromFile(File(imagePath!!)))
        }
    }
}
