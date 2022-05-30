package com.example.map_todo.model

import java.io.Serializable

data class places(val title: String?, val description: String?, val latitude: Double, val longitude: Double) : Serializable
