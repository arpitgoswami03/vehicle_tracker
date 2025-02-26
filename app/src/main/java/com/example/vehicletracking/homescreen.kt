package com.example.vehicletracking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vehicletracking.adapter.garage_car_adapter
import com.example.vehicletracking.data_class.carDetails
import com.example.vehicletracking.databinding.ActivityHomescreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class homescreen : AppCompatActivity() {
    private lateinit var binding: ActivityHomescreenBinding
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDB: DatabaseReference = FirebaseDatabase.getInstance()
        .getReference("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomescreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentMail = firebaseAuth.currentUser?.email.toString()
        getUsername(currentMail) { username ->
            if (username != null) {
                binding.userName.text = username
                setUpCarList(username)
            } else {
                Toast.makeText(
                    this, "Unable to fetch userdata", Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.addcarBtn.setOnClickListener {
            val intent = Intent(this, addcarPage::class.java)
            startActivity(intent)
        }
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.map -> startActivity(
                    Intent(this, mapspage::class.java)
                )

                R.id.logout -> {
                    firebaseAuth.signOut()
                    startActivity(
                        Intent(this, loginpage::class.java)
                    )
                    finish()
                }
            }
            true
        }
        binding.profileArea.setOnClickListener {
            val intent = Intent(this, profilepage::class.java)
            startActivity(intent)
        }
    }

    private fun getUsername(currentUserEmail: String, callback: (String?) -> Unit) {
        firebaseDB.orderByChild("email").equalTo(currentUserEmail)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val username = snapshot.children.firstOrNull()
                        ?.child("username")
                        ?.getValue(String::class.java)
                    Log.i("Firebase", "Username found: $username")
                    callback(username)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Database error: ${error.message}")
                    callback(null)
                }
            })
    }

    private fun setUpCarList(username: String) {
        firebaseDB.child(username).child("Car Available")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val carList = snapshot.children.mapNotNull {
                        it.getValue(carDetails::class.java)
                    }.toMutableList()
                    setCarRV(carList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Database error: ${error.message}")
                }

            })

    }

    private fun setCarRV(carList: MutableList<carDetails>) {
        val adapter = garage_car_adapter(carList)
        binding.carRv.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
        binding.carRv.adapter = adapter
    }
}