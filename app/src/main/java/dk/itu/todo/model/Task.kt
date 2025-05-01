package dk.itu.todo.model

data class Task(
    val title: String,
    val description: String,
    val priority: Int,
    val isCompleted: Boolean,
    val imagePath: String? = null,
    val location: String? = null
)
