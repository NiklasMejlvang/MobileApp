package dk.itu.todo.model

//New Class for navigation

object LocationRepository {
    val savedLocations = mutableListOf<Location>()

    fun addLocation(location: Location) {
        savedLocations.add(location)
    }

    fun getAll(): List<Location> = savedLocations

    fun clearAll() {
        savedLocations.clear()
    }
}
