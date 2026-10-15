package com.example.map_todo.ui.list

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.map_todo.EXTRA_MAP_TITLE
import com.example.map_todo.EXTRA_USER_MAP
import com.example.map_todo.R
import com.example.map_todo.data.MapRepository
import com.example.map_todo.data.model.UserMap
import com.example.map_todo.databinding.ActivityMainBinding
import com.example.map_todo.databinding.DialogCreateMapBinding
import com.example.map_todo.ui.create.CreateMapActivity
import com.example.map_todo.ui.display.DisplayMapActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: MapRepository
    private lateinit var mapsAdapter: MapsAdapter
    private val userMaps = mutableListOf<UserMap>()

    private val createMapLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult
            val userMap = result.data?.getSerializableExtra(EXTRA_USER_MAP) as? UserMap
                ?: return@registerForActivityResult
            userMaps.add(userMap)
            mapsAdapter.notifyItemInserted(userMaps.size - 1)
            repository.saveMaps(userMaps)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = MapRepository(this)
        userMaps.addAll(repository.loadMaps())

        mapsAdapter = MapsAdapter(userMaps) { position -> openMap(userMaps[position]) }
        binding.rvMaps.layoutManager = LinearLayoutManager(this)
        binding.rvMaps.adapter = mapsAdapter

        binding.fabCreateMap.setOnClickListener { showCreateMapDialog() }
    }

    private fun openMap(userMap: UserMap) {
        val intent = Intent(this, DisplayMapActivity::class.java)
        intent.putExtra(EXTRA_USER_MAP, userMap)
        startActivity(intent)
    }

    private fun showCreateMapDialog() {
        val dialogBinding = DialogCreateMapBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.dialog_create_map_title)
            .setView(dialogBinding.root)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.save, null)
            .show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val title = dialogBinding.etMapTitle.text.toString().trim()
            if (title.isEmpty()) {
                Toast.makeText(this, R.string.error_empty_map_title, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val intent = Intent(this, CreateMapActivity::class.java)
            intent.putExtra(EXTRA_MAP_TITLE, title)
            createMapLauncher.launch(intent)
            dialog.dismiss()
        }
    }
}
