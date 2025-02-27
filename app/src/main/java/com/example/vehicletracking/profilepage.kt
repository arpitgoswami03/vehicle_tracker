package com.example.vehicletracking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vehicletracking.databinding.ActivityProfilepageBinding

class profilepage : AppCompatActivity(){
    private lateinit var binding: ActivityProfilepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilepageBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}