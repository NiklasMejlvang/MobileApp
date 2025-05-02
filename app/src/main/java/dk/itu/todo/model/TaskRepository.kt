package dk.itu.todo.model

import android.content.ContentValues
import android.content.Context
import android.util.Log
import dk.itu.todo.model.database.DBCreate
import dk.itu.todo.model.database.TaskCursorWrapper
import dk.itu.todo.model.database.TasksDbSchema.TaskTable

class TaskRepository(context: Context) {
    private val dbHelper = DBCreate(context)

    fun getAllTasks(): List<Task> {
        val db = dbHelper.readableDatabase

        // — debug: list actual columns in your tasks table —
        db.rawQuery("PRAGMA table_info(${TaskTable.NAME});", null).use { pragmaCursor ->
            val cols = mutableListOf<String>()
            while (pragmaCursor.moveToNext()) {
                // "name" is the column in the pragma result that holds each column’s name
                cols += pragmaCursor.getString(
                    pragmaCursor.getColumnIndexOrThrow("name")
                )
            }
            Log.d("DB_SCHEMA", "Columns in ${TaskTable.NAME}: $cols")
        }


        val cursor = db.query(
            TaskTable.NAME, null, null, null,
            null, null, null
        )
        val tasks = mutableListOf<Task>()
        val wrappedCursor = TaskCursorWrapper(cursor)
        while (wrappedCursor.moveToNext()) {
            tasks.add(wrappedCursor.task)
        }
        cursor.close()
        return tasks
    }

    fun addTask(task: Task) {
        val db = dbHelper.writableDatabase
        db.execSQL(
            "INSERT INTO ${TaskTable.NAME} (" +
                    "${TaskTable.Cols.TITLE}, " +
                    "${TaskTable.Cols.DESCRIPTION}, " +
                    "${TaskTable.Cols.PRIORITY}, " +
                    "${TaskTable.Cols.IS_COMPLETED}, " +
                    "${TaskTable.Cols.IMAGE_PATH}, " +
                    "${TaskTable.Cols.LOCATION}" +
                    ") VALUES (?, ?, ?, ?, ?, ?)",
            arrayOf(task.title, task.description, task.priority, if (task.isCompleted) 1 else 0, task.imagePath, task.location)
        )

    }

    fun deleteTask(title: String) {
        val db = dbHelper.writableDatabase
        db.delete(TaskTable.NAME, "${TaskTable.Cols.TITLE} = ?", arrayOf(title))
    }
}
