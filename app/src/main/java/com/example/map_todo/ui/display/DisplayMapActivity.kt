package com.example.map_todo.ui.display

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.map_todo.EXTRA_USER_MAP
import com.example.map_todo.R
import com.example.map_todo.data.model.UserMap
import com.example.map_todo.databinding.ActivityDisplayMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

private const val MAP_PADDING_PX = 100

class DisplayMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityDisplayMapBinding
    private lateinit var userMap: UserMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userMap = intent.getSerializableExtra(EXTRA_USER_MAP) as UserMap
        supportActionBar?.title = userMap.title

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if (userMap.places.isEmpty()) return

        val boundsBuilder = LatLngBounds.Builder()
        for (place in userMap.places) {
            val latLng = LatLng(place.latitude, place.longitude)
            boundsBuilder.include(latLng)
            googleMap.addMarker(
                MarkerOptions().position(latLng).title(place.title).snippet(place.description)
            )
        }
        val metrics = resources.displayMetrics
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                boundsBuilder.build(), metrics.widthPixels, metrics.heightPixels, MAP_PADDING_PX
            )
        )
    }
}
