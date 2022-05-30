package com.example.map_todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.map_todo.databinding.ActivityGooglemapBinding
import com.example.map_todo.model.user
import com.google.android.gms.maps.model.LatLngBounds

private const val TAG = "onMapReady"
class googlemap : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityGooglemapBinding
    private lateinit var userMap: user
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGooglemapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userMap = intent.getSerializableExtra(EXTRA_MAP_VALUE) as user
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val boundbuilder = LatLngBounds.Builder()
        Log.i(TAG, "user map to render: ${userMap.title}")
        // Add a marker in Sydney and move the camera
        for(place in userMap.place)
        {
            var sydney = LatLng(place.latitude, place.longitude)
            boundbuilder.include(sydney)
            mMap.addMarker(MarkerOptions().position(sydney).title(userMap.title).snippet(place.description))
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundbuilder.build(), 1000, 1000, 0))
    }
}