package com.example.vehicletracking

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.vehicletracking.databinding.ActivityHomescreenBinding
import com.google.firebase.auth.FirebaseAuth

class homescreen : AppCompatActivity() {
    private lateinit var binding : ActivityHomescreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Vehicle Tracker"
        binding = ActivityHomescreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUser = FirebaseAuth.getInstance().currentUser?.email.toString()
        Log.i("currentUser", currentUser)
        binding.homenavbar.selectedItemId = R.id.garage
        binding.homenavbar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.map -> setFragment(mapFragment())
                R.id.garage -> setFragment(garageFragment())
                R.id.profile -> setFragment(profileFragment())
            }
            true
        }
    }
    private fun setFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.main, fragment)
            .commit()
    }
}