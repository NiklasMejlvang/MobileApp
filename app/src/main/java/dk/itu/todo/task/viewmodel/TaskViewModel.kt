package dk.itu.todo.task.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dk.itu.todo.model.Location
import dk.itu.todo.model.Task
import dk.itu.todo.model.TaskRepository

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TaskRepository(application)

    fun addTask(
        title: String,
        description: String,
        priority: Int,
        imagePath: String?,
        location: Location?,
        latitude: Double = 0.0,
        longitude: Double = 0.0
    ) {
        val task = Task(
            title = title,
            description = description,
            priority = priority,
            imagePath = imagePath,
            isCompleted = false,
            location = location?.name,
            latitude = location?.latitude ?: latitude,
            longitude = location?.longitude ?: longitude
        )
        repository.addTask(task)
    }
    fun updateTask(
        id: Long,
        title: String,
        description: String,
        priority: Int,
        isCompleted: Boolean,
        imagePath: String?,
        location: Location?,
        latitude: Double = 0.0,
        longitude: Double = 0.0
    ) {
        val updated = Task(
            id = id,
            title = title,
            description = description,
            priority = priority,
            isCompleted = isCompleted,
            imagePath = imagePath,
            location = location?.name,
            latitude = location?.latitude ?: latitude,
            longitude = location?.longitude ?: longitude
        )
        repository.updateTask(updated)
    }
}
