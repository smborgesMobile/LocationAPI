package br.com.gps.sample.locationsample

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val PERMISSION_ID = 3434

    private lateinit var focusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        get_location.setOnClickListener {
            getLastLocation()
        }

        focusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun getLastLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                focusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location: Location? = task.result

                    if (location == null) {

                    } else {
                        get_text_description.text = "Latitude: ${location.latitude} Longitude: ${location.longitude}"
                    }
                }
            } else {
                Log.d("SAMPLE", "Location is not enabled")
            }
        } else {
            requestPermission()
        }
    }

    // Verificar as permissões
    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    // Requistar permissões para o usuário.
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_ID)
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("SAMPLE", "you have pewrmissions")
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}