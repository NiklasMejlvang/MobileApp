package dk.itu.todo.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDB(context: Context?)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE Tasks (" +
                    "TaskID      INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Title       TEXT, " +
                    "Description TEXT, " +
                    "Priority    TEXT, " +
                    "IsCompleted INTEGER" +
                    ");"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // nothing for now
    }

    companion object {
        private const val VERSION = 1
        const val DATABASE_NAME = "Tasks.db"
    }
}
