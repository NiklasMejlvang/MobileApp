package dk.itu.todo.model.database

class TasksDbSchema {
    object TaskTable {
        const val NAME = "Tasks"

        object Cols {
            const val TITLE = "title"
            const val DESCRIPTION = "description"
            const val PRIORITY = "priority"
            const val IS_COMPLETED = "isCompleted"
        }
    }
}