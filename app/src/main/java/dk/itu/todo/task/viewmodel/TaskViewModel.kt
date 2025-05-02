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
        location: Location?
    ) {
        val task = Task(
            title = title,
            description = description,
            priority = priority,
            imagePath = imagePath,
            isCompleted = false,
            location = location?.name
        )
        repository.addTask(task)
    }
    fun updateTask(
        oldTitle: String,
        title: String,
        description: String,
        priority: Int,
        imagePath: String?,
        location: Location?
    ) {
        val updated = Task(
            title = title,
            description = description,
            priority = priority,
            isCompleted = false,       // or preserve from a var you captured earlier
            imagePath = imagePath,
            location = location?.name
        )
        repository.updateTask(oldTitle, updated)
    }
}
