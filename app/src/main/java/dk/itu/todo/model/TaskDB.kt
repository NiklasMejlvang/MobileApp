package dk.itu.todo.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDB(context: Context?)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {

    companion object {
        private const val VERSION = 1
        const val DATABASE_NAME = "Tasks.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
      CREATE TABLE Tasks (
        TaskID      INTEGER PRIMARY KEY AUTOINCREMENT,
        Title       TEXT,
        Description TEXT,
        Priority    INTEGER,
        IsCompleted INTEGER,
        ImagePath   TEXT
      );
      """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // no-op; fresh install
    }
}
