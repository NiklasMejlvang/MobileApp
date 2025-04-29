package dk.itu.todo.model.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import dk.itu.todo.model.database.TasksDbSchema.TaskTable

class DBCreate(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {
    companion object {
        private const val VERSION = 1
        const val DATABASE_NAME = "tasks.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table " + TaskTable.NAME + "(" +
                    TaskTable.Cols.TITLE + " text not null, " +
                    TaskTable.Cols.DESCRIPTION + " text not null, " +
                    TaskTable.Cols.PRIORITY + " integer not null, " +
                    TaskTable.Cols.IS_COMPLETED + " integer not null);"
        )
    }

    private fun addItem(db: SQLiteDatabase, what: String, where: String) {
        db.execSQL("INSERT INTO " + TaskTable.NAME + " (" +
                TaskTable.Cols.TITLE + ", " +
                TaskTable.Cols.DESCRIPTION + ", " +
                TaskTable.Cols.PRIORITY + ", " +
                TaskTable.Cols.IS_COMPLETED + ") VALUES (?,?,?,?)",
            arrayOf(what, where, 0, 0)
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}