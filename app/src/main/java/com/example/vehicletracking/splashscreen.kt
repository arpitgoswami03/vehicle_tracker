package com.example.vehicletracking

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vehicletracking.databinding.ActivitySplashscreenBinding

class splashscreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashscreenBinding
    private var handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handler.postDelayed(
            {
                val intent = Intent(this,loginpage::class.java)
                startActivity(intent)
                finish()
            },
            3000
        )
    }
}