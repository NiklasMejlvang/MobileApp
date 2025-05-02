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
}
