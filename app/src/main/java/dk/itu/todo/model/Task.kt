package dk.itu.todo.model

//New class for navigation

data class Task(
    var title: String,
    var description: String,
    var priority: Int,
    var isCompleted: Boolean = false,
)
