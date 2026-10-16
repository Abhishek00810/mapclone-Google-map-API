package com.example.map_todo.ui.create

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.map_todo.EXTRA_MAP_TITLE
import com.example.map_todo.EXTRA_USER_MAP
import com.example.map_todo.R
import com.example.map_todo.data.model.Place
import com.example.map_todo.data.model.UserMap
import com.example.map_todo.databinding.ActivityCreateMapBinding
import com.example.map_todo.databinding.DialogCreatePlaceBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class CreateMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityCreateMapBinding
    private lateinit var mapTitle: String
    private val markers = mutableListOf<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapTitle = intent.getStringExtra(EXTRA_MAP_TITLE).orEmpty()
        supportActionBar?.title = mapTitle

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mapFragment.view?.let {
            Snackbar.make(it, R.string.hint_long_press_to_add_marker, Snackbar.LENGTH_LONG)
                .setAction(R.string.ok) {}
                .setActionTextColor(ContextCompat.getColor(this, android.R.color.white))
                .show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_create_map, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mi_save) {
            saveMap()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnInfoWindowClickListener { marker ->
            markers.remove(marker)
            marker.remove()
        }
        map.setOnMapLongClickListener { latLng -> showCreatePlaceDialog(latLng) }
    }

    private fun saveMap() {
        if (markers.isEmpty()) {
            Toast.makeText(this, R.string.error_no_markers, Toast.LENGTH_LONG).show()
            return
        }
        val places = markers.map { marker ->
            Place(
                marker.title.orEmpty(),
                marker.snippet.orEmpty(),
                marker.position.latitude,
                marker.position.longitude
            )
        }
        val data = Intent().putExtra(EXTRA_USER_MAP, UserMap(mapTitle, places))
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    private fun showCreatePlaceDialog(latLng: LatLng) {
        val dialogBinding = DialogCreatePlaceBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.dialog_create_marker_title)
            .setView(dialogBinding.root)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.save, null)
            .show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val title = dialogBinding.etTitle.text.toString().trim()
            val description = dialogBinding.etDescription.text.toString().trim()
            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, R.string.error_empty_place_fields, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            map.addMarker(MarkerOptions().position(latLng).title(title).snippet(description))
                ?.let { markers.add(it) }
            dialog.dismiss()
        }
    }
}
