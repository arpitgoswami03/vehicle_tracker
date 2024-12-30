package com.example.vehicletracking

data class loginDetails(
    val name: String,
    val username: String,
    val email: String,
    val password: String
)

data class carDetails(
    val name: String,
    val model: String,
    val number: String,
    val imageUrl: String
)
