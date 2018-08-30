package de.stuermerbenjamin.livedata

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

@SuppressLint("MissingPermission")
class DeviceLocationObserver(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    private val listener: LocationListener
) : LifecycleObserver {

    private val locationManager = context.applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Long.MIN_VALUE, Float.MIN_VALUE, listener)

        val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (lastLocation != null) {
            listener.onLocationChanged(lastLocation)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        locationManager.removeUpdates(listener)
    }
}
