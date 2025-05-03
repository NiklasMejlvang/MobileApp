package dk.itu.todo.model

data class Task (
    var id: Long = 0L,
    val title: String,
    val description: String,
    var priority: Int,
    var isCompleted: Boolean,
    val imagePath: String? = null,
    val location: String? = null
) {
    init {
        if (priority < 1) priority = 1
        if (priority > 5) priority = 5
    }
}
