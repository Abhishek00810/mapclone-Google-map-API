package com.example.map_todo

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.map_todo.model.places
import com.example.map_todo.model.user
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

private const val TAG = "MainActivity"
const val EXTRA_MAP_VALUE = "Map_value"
private const val FILENAME = "userdata.data"
 var EXTRA_MAP_TITLE = "Map_Title"
const val requestcode = 1234

private lateinit var userMaps : MutableList<user>
private  lateinit var mapAdapter: mapadapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userMaps = deseriliable(this).toMutableList()
        //set layout manager (xml file
        rvmaps.layoutManager = LinearLayoutManager(this)
        //set adapter class
        mapAdapter = mapadapter(this, userMaps, object: mapadapter.OnclickListener{
            override fun onItemClick(position: Int) {
                Log.i(TAG, "On item $position")
                val intent = Intent(this@MainActivity, googlemap::class.java);
                intent.putExtra(EXTRA_MAP_VALUE, userMaps[position])
                startActivity(intent)
            }
        })
        rvmaps.adapter = mapAdapter
        fabcreatemap.setOnClickListener{
            Log.i(TAG, "Clicked on FB")
            showalertdialgoe()
        }

    }
    private fun showalertdialgoe() {
        val mapformview = LayoutInflater.from(this).inflate(R.layout.diaglog_create_map, null)
        val dialogue = AlertDialog.Builder(this)
            .setTitle("Map Title")
            .setView(mapformview)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Save", null)
            .show()

        dialogue.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener{
            val title = mapformview.findViewById<EditText>(R.id.st_title).text.toString()
            if(title.trim().isEmpty()) {
                Toast.makeText(
                    this,
                    "Title place should be non empty",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            //navigate to map activity
            val intent = Intent(this@MainActivity, CreateMapActivity::class.java);
            EXTRA_MAP_TITLE = title
            intent.putExtra(EXTRA_MAP_TITLE, title)
            startActivityForResult(intent, requestcode)
            dialogue.dismiss()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestcode == requestCode && resultCode == Activity.RESULT_OK)
        {
            val userMap = data?.getSerializableExtra(EXTRA_MAP_VALUE) as user
            Log.i(TAG, "Working data transfer ${userMap.title}")
            userMaps.add(userMap)
            mapAdapter.notifyItemInserted(userMaps.size - 1)
            serilizablemaps(this, userMaps)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private  fun serilizablemaps(context: Context, usermap: List<user>)
    {
        Log.i(TAG, "SERILIZABLE")
        ObjectOutputStream(FileOutputStream(getDatafile(context))).use { it.writeObject(userMaps) }
    }

    private fun deseriliable(context: Context) : List<user>
    {
        Log.i(TAG, "DERSLABLE")
        val datafile = getDatafile(context)
        if(!datafile.exists())
        {
            Log.i(TAG, "DATA FILE DOESNT EXIST")
            return emptyList()
        }
        ObjectInputStream(FileInputStream(datafile)).use {return it.readObject() as List<user>}
    }
    private fun getDatafile(context: Context) : File {
        Log.i(TAG, "getting file from directory $context.filesDir")
        return File(context.filesDir, FILENAME)
    }

}
private fun generateSampleData(): List<user> {
    return listOf(
        user(
            "Memories from University",
            listOf(
                places("Branner Hall", "Best dorm at Stanford", 37.426, -122.163),
                places("Gates CS building", "Many long nights in this basement", 37.430, -122.173),
                places("Pinkberry", "First date with my wife", 37.444, -122.170)
            )
        ),
        user("January vacation planning!",
            listOf(
                places("Tokyo", "Overnight layover", 35.67, 139.65),
                places("Ranchi", "Family visit + wedding!", 23.34, 85.31),
                places("Singapore", "Inspired by \"Crazy Rich Asians\"", 1.35, 103.82)
            )),
        user("Singapore travel itinerary",
            listOf(
                places("Gardens by the Bay", "Amazing urban nature park", 1.282, 103.864),
                places("Jurong Bird Park", "Family-friendly park with many varieties of birds", 1.319, 103.706),
                places("Sentosa", "Island resort with panoramic views", 1.249, 103.830),
                places("Botanic Gardens", "One of the world's greatest tropical gardens", 1.3138, 103.8159)
            )
        ),
        user("My favorite places in the Midwest",
            listOf(
                places("Chicago", "Urban center of the midwest, the \"Windy City\"", 41.878, -87.630),
                places("Rochester, Michigan", "The best of Detroit suburbia", 42.681, -83.134),
                places("Mackinaw City", "The entrance into the Upper Peninsula", 45.777, -84.727),
                places("Michigan State University", "Home to the Spartans", 42.701, -84.482),
                places("University of Michigan", "Home to the Wolverines", 42.278, -83.738)
            )
        ),
        user("Restaurants to try",
            listOf(
                places("Champ's Diner", "Retro diner in Brooklyn", 40.709, -73.941),
                places("Althea", "Chicago upscale dining with an amazing view", 41.895, -87.625),
                places("Shizen", "Elegant sushi in San Francisco", 37.768, -122.422),
                places("Citizen Eatery", "Bright cafe in Austin with a pink rabbit", 30.322, -97.739),
                places("Kati Thai", "Authentic Portland Thai food, served with love", 45.505, -122.635)
            )
        )
    )
}