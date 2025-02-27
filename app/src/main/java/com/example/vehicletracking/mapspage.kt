package com.example.vehicletracking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vehicletracking.databinding.ActivityMappageBinding

class mapspage: AppCompatActivity() {
    private lateinit var binding: ActivityMappageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMappageBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}