package dk.itu.todo.model.database

import android.database.Cursor
import android.database.CursorWrapper
import dk.itu.todo.model.Task
import dk.itu.todo.model.database.TasksDbSchema.TaskTable

class TaskCursorWrapper(cursor: Cursor?) : CursorWrapper(cursor) {
    val task: Task
        get() {
            val id = getLong(getColumnIndexOrThrow(TaskTable.Cols.ID))
            val title = getString(getColumnIndex(TaskTable.Cols.TITLE))
            val description = getString(getColumnIndex(TaskTable.Cols.DESCRIPTION))
            val priority = getInt(getColumnIndex(TaskTable.Cols.PRIORITY))
            val isCompleted = getInt(getColumnIndex(TaskTable.Cols.IS_COMPLETED)) != 0
            val imagePath = getString(getColumnIndex(TaskTable.Cols.IMAGE_PATH))
            val location = getString(getColumnIndexOrThrow(TaskTable.Cols.LOCATION))
            val latitude = getDouble(getColumnIndexOrThrow(TaskTable.Cols.LATITUDE))
            val longitude = getDouble(getColumnIndexOrThrow(TaskTable.Cols.LONGITUDE))
            return Task(id, title, description, priority, isCompleted, imagePath, location, latitude, longitude)
        }
}
