package com.flx.features.location.logic

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import org.fuusio.api.feature.FeatureManager

class LocationManager(val context: Context) :
    FeatureManager(),
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    com.google.android.gms.location.LocationListener {

    private val listeners = mutableSetOf<LocationListener>()

    private val locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private lateinit var googleApiClient: GoogleApiClient
    private lateinit var locationRequest: LocationRequest

    fun addListener(listener: LocationListener) {
        listeners.add(listener)
    }

    fun locationListener(listener: LocationListener) {
        listeners.remove(listener)
    }

    override fun onConnected(p0: Bundle?) {

        startLocationUpdates()

        val fusedLocationProviderClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            OnSuccessListener<Location> { location ->
                if (location != null) {
                    listeners.forEach { listener -> listener.onLocationChanged(location) }
                }
            }
        }
    }

    private fun startLocationUpdates() {
        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = UPDATE_INTERVAL
            fastestInterval = FASTEST_UPDATE_INTERVAL
        }
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        // fusedLocationClient.requestLocationUpdates(locationRequest, this)
    }

    fun initLocation() {
        googleApiClient = GoogleApiClient.Builder(context).apply {
            addConnectionCallbacks(this@LocationManager)
            addConnectionCallbacks(this@LocationManager)
            addApi(LocationServices.API)
        }.build()
        checkLocation()
    }

    private fun checkLocation() {
        TODO("Not yet implemented")
    }

    fun isLocationEnabled(): Boolean =
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    override fun onConnectionSuspended(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }

    override fun onLocationChanged(location: Location) {
        listeners.forEach { listener -> listener.onLocationChanged(location) }
    }

    companion object {
        const val UPDATE_INTERVAL = 5000L
        const val FASTEST_UPDATE_INTERVAL = 1000L
    }
}