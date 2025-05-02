package dk.itu.todo.taskList.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dk.itu.todo.model.Task
import dk.itu.todo.model.TaskRepository

class TaskListViewModel(application: Application)
    : AndroidViewModel(application) {

    private val repository = TaskRepository(application)
    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks

    fun loadTasks() { _tasks.value = repository.getAllTasks() }
    fun deleteTask(title: String) {
        repository.deleteTask(title)
        loadTasks()
    }
}
