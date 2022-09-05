package com.messaging.safaltatask

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.messaging.safaltatask.databinding.ActivityShowHospitalBinding
import com.messaging.safaltatask.utils.FetchData

class ShowHospitalActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityShowHospitalBinding
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var currentLocation: Location
    private val Req_Code: Int = 39
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowHospitalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mFusedLocationClient=LocationServices.getFusedLocationProviderClient(this@ShowHospitalActivity)
        getCurrentLocation()

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("You are here!")
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f))
        mMap.addMarker(markerOptions)

        val stringBuilder=StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
        stringBuilder.append("location="+currentLocation.latitude+","+currentLocation.longitude)
        stringBuilder.append("&radius=3000")
        stringBuilder.append("&type=hospital")
        stringBuilder.append("&sensor=true")
        stringBuilder.append("&key="+resources.getString(R.string.google_api_key))
        val url=stringBuilder.toString()
        val dataFetch= arrayOf(mMap,url)
        val fetchData=FetchData()
        fetchData.execute(dataFetch)


    }
    private fun getCurrentLocation(){
          if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
              && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
          {
              ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),Req_Code)
              return
          }

              val task = mFusedLocationClient.lastLocation
              task.addOnSuccessListener {location ->
                  if (location != null) {
                      currentLocation = location
                      val mapFragment = supportFragmentManager
                          .findFragmentById(R.id.map) as SupportMapFragment
                      mapFragment.getMapAsync(this@ShowHospitalActivity)
                  }
              }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            Req_Code ->
            {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    getCurrentLocation()

            }

        }
    }
}