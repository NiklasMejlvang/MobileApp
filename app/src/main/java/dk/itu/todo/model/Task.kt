package dk.itu.todo.model

//New class for navigation

data class Task(
    val title: String,
    val description: String,
    val priority: Int,
    val isCompleted: Boolean,
    val imagePath: String? = null,
    val location: String? = null
)