package com.example.map_todo.data.model

import java.io.Serializable

data class UserMap(val title: String, val places: List<Place>) : Serializable
