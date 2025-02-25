package com.example.vehicletracking.data_class

data class carDetails(
    val name: String,
    val model: String,
    val number: String,
    val imageUrl: String
){
    constructor() : this("", "", "", "")
}
