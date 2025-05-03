package dk.itu.todo.model.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import dk.itu.todo.model.database.TasksDbSchema.TaskTable
import dk.itu.todo.model.database.TasksDbSchema.TaskTable.Cols


class DBCreate(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {
    companion object {
        private const val VERSION = 4
        const val DATABASE_NAME = "tasks.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE " + TaskTable.NAME + "(" +
                    TaskTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TaskTable.Cols.TITLE + " TEXT NOT NULL, " +
                    TaskTable.Cols.DESCRIPTION + " TEXT NOT NULL, " +
                    TaskTable.Cols.PRIORITY + " INTEGER NOT NULL, " +
                    TaskTable.Cols.IS_COMPLETED + " INTEGER NOT NULL, " +
                    TaskTable.Cols.IMAGE_PATH + " TEXT, " +
                    TaskTable.Cols.LOCATION + " TEXT" +
                    ");"
        )
        db.execSQL(
            "CREATE TABLE Locations (" +
                    "name TEXT NOT NULL, " +
                    "latitude REAL NOT NULL, " +
                    "longitude REAL NOT NULL" +
                    ");"
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

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL(
                "ALTER TABLE ${TaskTable.NAME} ADD COLUMN ${Cols.IMAGE_PATH} TEXT;"
            )
            db.execSQL(
                "ALTER TABLE ${TaskTable.NAME} ADD COLUMN ${Cols.LOCATION} TEXT;"
            )
        }


        if (oldVersion < 3) {
            db.execSQL("DROP TABLE IF EXISTS ${TaskTable.NAME}")
            onCreate(db)

        }
        }
}