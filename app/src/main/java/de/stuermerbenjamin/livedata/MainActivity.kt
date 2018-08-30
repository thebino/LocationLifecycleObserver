package de.stuermerbenjamin.livedata

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

private const val REQUEST_CODE_LOCATION_PERMISSION: Int = 135

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "This app requires Location access!", Toast.LENGTH_LONG).show()
            } else {
                requestPermission()
            }
        } else {
            getLocationUpdates()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                    getLocationUpdates()
                } else {
                    // permission denied
                    val snackbar = Snackbar.make(
                            coordinatorLayout,
                            R.string.location_permission_denied, Snackbar.LENGTH_INDEFINITE)
                    snackbar.setAction(R.string.location_permission_request_button) {
                        requestPermission()
                    }
                    snackbar.show()
                }
            }
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE_LOCATION_PERMISSION)
    }

    private fun getLocationUpdates() {
        DeviceLocationObserver(this, this, object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                location?.let {
                    textViewLatitude.text = getString(R.string.latitude, it.latitude)
                    textViewLongitude.text = getString(R.string.longitude, it.longitude)
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

            override fun onProviderEnabled(provider: String?) {}

            override fun onProviderDisabled(provider: String?) {}
        })
    }
}
