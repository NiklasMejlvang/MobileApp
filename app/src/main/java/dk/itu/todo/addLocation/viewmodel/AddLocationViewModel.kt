package dk.itu.todo.addLocation.viewmodel

import android.app.Application
import android.location.Geocoder
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import dk.itu.todo.model.Location
import dk.itu.todo.model.LocationRepository
import java.io.IOException
import java.util.Locale

class AddLocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationRepository = LocationRepository(application)
    val locationResult = MutableLiveData<Result<Location>>()

    fun addLocation(name: String, address: String) {
        Thread {
            val geocoder = Geocoder(getApplication(), Locale.getDefault())
            val results =
                try {
                    if (Geocoder.isPresent()) {
                        geocoder.getFromLocationName(address, 1)
                    } else {
                        null
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    null
                }
            if (!results.isNullOrEmpty()) {
                val location = results[0]
                val latitude = location.latitude
                val longitude = location.longitude

                val newLocation = Location(name, latitude, longitude)
                locationRepository.addLocation(newLocation)

                Handler(Looper.getMainLooper()).post {
                    locationResult.value = Result.success(newLocation)
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    locationResult.value = Result.failure(Exception("Could not find location"))
                }
            }
        }.start()
    }
}
