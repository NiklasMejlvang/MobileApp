package dk.itu.todo.model.database

import android.database.Cursor
import android.database.CursorWrapper
import dk.itu.todo.model.Task
import dk.itu.todo.model.database.TasksDbSchema.TaskTable

class TaskCursorWrapper(cursor: Cursor) : CursorWrapper(cursor) {
    val task: Task
        get() {
            val title       = getString   (getColumnIndexOrThrow(TaskTable.Cols.TITLE))
            val description = getString   (getColumnIndexOrThrow(TaskTable.Cols.DESCRIPTION))
            val priority    = getInt      (getColumnIndexOrThrow(TaskTable.Cols.PRIORITY))
            val isCompleted = getInt      (getColumnIndexOrThrow(TaskTable.Cols.IS_COMPLETED)) != 0
            val imagePath   = getString   (getColumnIndexOrThrow(TaskTable.Cols.IMAGE_PATH))
            return Task(title, description, priority, isCompleted, imagePath)
        }
}
