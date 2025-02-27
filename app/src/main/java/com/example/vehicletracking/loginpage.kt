package com.example.vehicletracking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vehicletracking.databinding.ActivityLoginpageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class loginpage : AppCompatActivity() {
    private lateinit var binding: ActivityLoginpageBinding
    private val database = FirebaseDatabase.getInstance()
    private val reference = database.getReference("Users")
    private val authenticator = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginpageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupGo.setOnClickListener {
            val intent = Intent(this, signuppage::class.java)
            startActivity(intent)
        }
        binding.loginButton.setOnClickListener {
            val emailT = binding.email.text.toString()
            val passwordT = binding.password.text.toString()

            if (emailT.isNotEmpty() && passwordT.isNotEmpty()) {
                loginWithFirebase(emailT, passwordT)
            } else {
                Toast.makeText(
                    this, "Please fill all the fields", Toast.LENGTH_SHORT
                )
                    .show()

            }
        }
    }

    private fun loginWithFirebase(emailT: String, passwordT: String) {
        authenticator.signInWithEmailAndPassword(emailT, passwordT)
            .addOnSuccessListener {
                Toast.makeText(
                    this, "Login Successful", Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(applicationContext, homescreen::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this, "Login Failed", Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onResume() {
        super.onResume()
        val username = authenticator.currentUser
        Log.e("FirebaseL", "$username")
        if (username != null) {
            startActivity(Intent(applicationContext, homescreen::class.java))
            finish()
        }
    }

}