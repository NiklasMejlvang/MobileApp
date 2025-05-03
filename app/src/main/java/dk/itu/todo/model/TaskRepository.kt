package dk.itu.todo.model

import android.content.ContentValues
import android.content.Context
import android.util.Log
import dk.itu.todo.model.database.DBCreate
import dk.itu.todo.model.database.TaskCursorWrapper
import dk.itu.todo.model.database.TasksDbSchema.TaskTable
import dk.itu.todo.model.database.TasksDbSchema.TaskTable.Cols

class TaskRepository(context: Context) {
    private val dbHelper = DBCreate(context)

    fun getAllTasks(): List<Task> {
        val db = dbHelper.readableDatabase

        db.rawQuery("PRAGMA table_info(${TaskTable.NAME});", null).use { pragmaCursor ->
            val cols = mutableListOf<String>()
            while (pragmaCursor.moveToNext()) {
                cols += pragmaCursor.getString(
                    pragmaCursor.getColumnIndexOrThrow("name")
                )
            }
            Log.d("DB_SCHEMA", "Columns in ${TaskTable.NAME}: $cols")
        }

        val cursor = db.query(
            TaskTable.NAME, null, null, null,
            null, null, "${Cols.IS_COMPLETED} ASC, ${Cols.PRIORITY} ASC"

        )
        val tasks = mutableListOf<Task>()
        val wrappedCursor = TaskCursorWrapper(cursor)
        while (wrappedCursor.moveToNext()) {
            tasks.add(wrappedCursor.task)
        }
        cursor.close()
        return tasks
    }

    fun addTask(task: Task): Long {
        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put(Cols.TITLE, task.title)
            put(Cols.DESCRIPTION, task.description)
            put(Cols.PRIORITY, task.priority)
            put(Cols.IS_COMPLETED, if (task.isCompleted) 1 else 0)
            put(Cols.IMAGE_PATH, task.imagePath)
            put(Cols.LOCATION, task.location)
        }
        val newId = db.insert(TaskTable.NAME, null, cv)
        task.id = newId
        return newId
    }

    fun deleteTask(id: Long) {
        val db = dbHelper.writableDatabase
        db.delete(TaskTable.NAME, "${Cols.ID} = ?", arrayOf(id.toString()))
    }


    fun updateTaskCompletion(task: Task) {
        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put(Cols.IS_COMPLETED, if (task.isCompleted) 1 else 0)
        }
        db.update(
            TaskTable.NAME,
            cv,
            "${Cols.ID} = ?",
            arrayOf(task.id.toString())
        )
    }

    fun updateTask(task: Task) {
        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put(Cols.TITLE,        task.title)
            put(Cols.DESCRIPTION,  task.description)
            put(Cols.PRIORITY,     task.priority)
            put(Cols.IS_COMPLETED, if (task.isCompleted) 1 else 0)
            put(Cols.IMAGE_PATH,   task.imagePath)
            put(Cols.LOCATION,     task.location)
        }
        db.update(
            TaskTable.NAME,
            cv,
            "${Cols.ID} = ?",
            arrayOf(task.id.toString())
        )
    }
}
