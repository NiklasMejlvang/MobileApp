package dk.itu.todo.model.database

class TasksDbSchema {
    object TaskTable {
        const val NAME = "Tasks"

        object Cols {
            const val ID = "id"
            const val TITLE = "title"
            const val DESCRIPTION = "description"
            const val PRIORITY = "priority"
            const val IS_COMPLETED = "isCompleted"
            const val IMAGE_PATH = "imagePath"
            const val LOCATION = "location"
        }
    }
}
