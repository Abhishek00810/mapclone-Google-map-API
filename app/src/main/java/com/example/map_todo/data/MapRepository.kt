package com.example.map_todo.data

import android.content.Context
import android.util.Log
import com.example.map_todo.data.model.UserMap
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

private const val TAG = "MapRepository"
private const val FILENAME = "user_maps.data"

/** Persists the user's maps to a file in the app's private storage. */
class MapRepository(context: Context) {

    private val dataFile = File(context.filesDir, FILENAME)

    fun loadMaps(): List<UserMap> {
        if (!dataFile.exists()) {
            return emptyList()
        }
        return try {
            ObjectInputStream(FileInputStream(dataFile)).use {
                @Suppress("UNCHECKED_CAST")
                it.readObject() as List<UserMap>
            }
        } catch (e: Exception) {
            Log.e(TAG, "Could not read saved maps", e)
            emptyList()
        }
    }

    fun saveMaps(maps: List<UserMap>) {
        try {
            ObjectOutputStream(FileOutputStream(dataFile)).use { it.writeObject(ArrayList(maps)) }
        } catch (e: IOException) {
            Log.e(TAG, "Could not save maps", e)
        }
    }
}
