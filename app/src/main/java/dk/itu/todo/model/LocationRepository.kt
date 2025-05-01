package dk.itu.todo.model

import android.content.Context
import dk.itu.todo.model.database.DBCreate

class LocationRepository(context: Context) {
    private val dbHelper = DBCreate(context)

    fun addLocation(location: Location) {
        val db = dbHelper.writableDatabase
        db.execSQL(
            "INSERT INTO Locations (name, latitude, longitude) VALUES (?, ?, ?)",
            arrayOf(location.name, location.latitude, location.longitude)
        )
    }

    fun getAll(): List<Location> {
        val db = dbHelper.readableDatabase
        val cursor = db.query("Locations", null, null, null, null, null, null)
        val locations = mutableListOf<Location>()
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val lat = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"))
            val lng = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"))
            locations.add(Location(name, lat, lng))
        }
        cursor.close()
        return locations
    }

    fun clearAll() {
        val db = dbHelper.writableDatabase
        db.delete("Locations", null, null)
    }
}
