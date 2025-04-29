package dk.itu.todo.model

data class Task(
    var title: String,
    var description: String,
    var priority: Int,
    var isCompleted: Boolean = false,
    var imagePath: String? = null
)
