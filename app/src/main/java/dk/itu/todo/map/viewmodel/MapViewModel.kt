package dk.itu.todo.map.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dk.itu.todo.model.Task
import dk.itu.todo.model.TaskRepository

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TaskRepository(application)

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks

    fun loadTasks() {
        val allTasks = repository.getAllTasks()
        _tasks.value = allTasks
    }
}
