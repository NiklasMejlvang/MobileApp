package dk.itu.todo.model

data class Task (
    var id: Long = 0L,
    val title: String,
    val description: String,
    var priority: Int,
    var isCompleted: Boolean,
    val imagePath: String? = null,
    val location: String? = null,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) {
    init {
        if (priority < 1) priority = 1
        if (priority > 5) priority = 5
    }
}
