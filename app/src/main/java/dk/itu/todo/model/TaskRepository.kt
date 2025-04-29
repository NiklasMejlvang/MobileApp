package dk.itu.todo.model

import android.content.ContentValues
import android.content.Context
import dk.itu.todo.model.database.DBCreate
import dk.itu.todo.model.database.TasksDbSchema.TaskTable

class TaskRepository(context: Context) {
    private val dbHelper = DBCreate(context)

    fun getAllTasks(): List<Task> {
        val db     = dbHelper.readableDatabase
        val cursor = db.query(TaskTable.NAME, null, null, null, null, null, null)
        val list   = mutableListOf<Task>()
        while (cursor.moveToNext()) {
            list += Task(
                title       = cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.Cols.TITLE)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.Cols.DESCRIPTION)),
                priority    = cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable.Cols.PRIORITY)),
                isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable.Cols.IS_COMPLETED)) == 1,
                imagePath   = cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.Cols.IMAGE_PATH))
            )
        }
        cursor.close()
        return list
    }

    fun addTask(task: Task) {
        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put(TaskTable.Cols.TITLE,        task.title)
            put(TaskTable.Cols.DESCRIPTION,  task.description)
            put(TaskTable.Cols.PRIORITY,     task.priority)
            put(TaskTable.Cols.IS_COMPLETED, if (task.isCompleted) 1 else 0)
            put(TaskTable.Cols.IMAGE_PATH,   task.imagePath)
        }
        db.insert(TaskTable.NAME, null, cv)
    }

    fun deleteTask(title: String) {
        val db = dbHelper.writableDatabase
        db.delete(TaskTable.NAME,
            "${TaskTable.Cols.TITLE} = ?",
            arrayOf(title))
    }
}
