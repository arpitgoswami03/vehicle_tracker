package com.example.vehicletracking

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vehicletracking.databinding.ActivityLoginpageBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class loginpage : AppCompatActivity() {
    private lateinit var binding: ActivityLoginpageBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val database = FirebaseDatabase.getInstance()
    private val reference = database.getReference("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginpageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        binding.signupGo.setOnClickListener {
            val intent = Intent(this, signuppage::class.java)
            startActivity(intent)
        }
        binding.loginButton.setOnClickListener {
            val usernameT = binding.username.text.toString()
            val passwordT = binding.password.text.toString()
            if (usernameT.isNotEmpty() && passwordT.isNotEmpty()) {
                Log.i("firebase", "All fields are filled")
                reference.child(usernameT).get().addOnSuccessListener {
                    if (it.exists()) {
                        val password = it.child("password").value
                        if (password != passwordT) {
                            Toast.makeText(
                                this, "Incorrect Password", Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            Toast.makeText(
                                this,
                                "Welcome ${it.child("name").value.toString()}",
                                Toast.LENGTH_SHORT
                            )
                                .show()

                            val editor = sharedPreferences.edit()
                            editor.apply {
                                this.putString("username", usernameT)
                                apply()
                            }

                            val intent = Intent(this, homescreen::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Toast.makeText(
                            this, "User doesn't exist", Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            } else {
                Toast.makeText(
                    this, "Please fill all the fields", Toast.LENGTH_SHORT
                )
                    .show()

            }
        }
    }
}