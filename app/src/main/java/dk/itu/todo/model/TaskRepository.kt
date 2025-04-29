package dk.itu.todo.model

import android.content.Context
import dk.itu.todo.model.database.DBCreate
import dk.itu.todo.model.database.TaskCursorWrapper
import dk.itu.todo.model.database.TasksDbSchema.TaskTable

class TaskRepository(context: Context) {
    private val dbHelper = DBCreate(context)

    fun getAllTasks(): List<Task> {
        val db = dbHelper.readableDatabase
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
                    "${TaskTable.Cols.IS_COMPLETED}) VALUES (?,?,?,?)",
            arrayOf(task.title, task.description, task.priority, if (task.isCompleted) 1 else 0)
        )
    }

    fun deleteTask(title: String) {
        val db = dbHelper.writableDatabase
        db.delete(TaskTable.NAME, "${TaskTable.Cols.TITLE} = ?", arrayOf(title))
    }
}