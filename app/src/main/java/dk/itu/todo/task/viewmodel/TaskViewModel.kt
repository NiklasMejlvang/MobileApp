package dk.itu.todo.task.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dk.itu.todo.model.Task
import dk.itu.todo.model.TaskRepository

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TaskRepository(application)

    fun addTask(
        title: String,
        description: String,
        priority: Int,
        isCompleted: Boolean,
        selectedLocation: Any?
    ) {
        val task = Task(
            title = title,
            description = description,
            priority = priority,
            isCompleted = isCompleted
        )
        repository.addTask(task)
    }
}
