package com.example.map_todo

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.map_todo.databinding.ActivityCreateMapBinding
import com.example.map_todo.model.places
import com.example.map_todo.model.user
import com.google.android.gms.maps.model.Marker
import com.google.android.material.snackbar.Snackbar

private const val TAG = "onMapReady"
class CreateMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityCreateMapBinding
    private var markers: MutableList<Marker> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = EXTRA_MAP_TITLE
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mapFragment.view?.let {
            Snackbar.make(it, "Long press to add a Marker", Snackbar.LENGTH_LONG)
                .setAction("OK", {})
                .setActionTextColor(ContextCompat.getColor(this, android.R.color.white))
                .setDuration(2000)
                .show(); // Don’t forget to show!
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_create_map, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.misave)
        {
            if(markers.isEmpty())
            {
                Toast.makeText(this, "There must be one marker", Toast.LENGTH_LONG).show()
                return true
            }
            val place = markers.map { marker ->  places(marker.title, marker.snippet, marker.position.latitude, marker.position.longitude)}
            val userMap = user(intent.getStringExtra(EXTRA_MAP_TITLE), place)
            val data = Intent()
            data.putExtra(EXTRA_MAP_VALUE, userMap)
            setResult(Activity.RESULT_OK, data)
            Log.i(TAG, "Is working?")
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
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
        mMap.setOnInfoWindowClickListener {markertodelete->
            Log.i(TAG, "Marker delete")
            markers.remove(markertodelete)
            markertodelete.remove()
        }
        mMap.setOnMapLongClickListener {latLng->
            Log.i(TAG, "Long click Pressed")
            showalertdialgoe(latLng)
        }
        // Add a marker in Sydney and move the camera
    }

    private fun showalertdialgoe(latLng: LatLng) {
        val placeforview = LayoutInflater.from(this).inflate(R.layout.diaglog_create_box, null)
        val dialogue = AlertDialog.Builder(this)
            .setTitle("Create a Market")
            .setView(placeforview)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Save", null)
            .show()

        dialogue.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener{
            val title = placeforview.findViewById<EditText>(R.id.ettitle).text.toString()
            val descr = placeforview.findViewById<EditText>(R.id.etdescription).text.toString()
            if(title.trim().isEmpty() || descr.trim().isEmpty())
            {
                Toast.makeText(this, "Title or description place should be non empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val marker = mMap.addMarker(MarkerOptions().position(latLng).title(title).snippet(descr))
            if (marker != null) {
                markers.add(marker)
            }
            dialogue.dismiss()
        }
    }
}