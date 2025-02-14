package com.example.vehicletracking

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vehicletracking.data_class.loginDetails
import com.example.vehicletracking.databinding.ActivitySignuppageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class signuppage : AppCompatActivity() {
    private lateinit var binding: ActivitySignuppageBinding
    private var database = FirebaseDatabase.getInstance()
    private var reference = database.getReference("Users")
    private var authenticator = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignuppageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.singupButton.setOnClickListener {
            val nameT = binding.name.text.toString()
            val usernameT = binding.username.text.toString()
            val emailT = binding.email.text.toString()
            val passwordT = binding.password.text.toString()

            val user = loginDetails(nameT, usernameT, emailT, passwordT)

            if(user.name.isNotEmpty() && user.username.isNotEmpty()
                && user.email.isNotEmpty() && user.password.isNotEmpty()){
                if(passwordT.length<8) {
                    Toast.makeText(
                        this
                        ,"Password should be at least 8 characters"
                        ,Toast.LENGTH_SHORT
                    )
                        .show()
                }

                if(!emailT.contains("@gmail.com")){
                    Toast.makeText(
                        this
                        ,"Please enter a valid email"
                        ,Toast.LENGTH_SHORT
                    )
                        .show()
                }

                createAccountWithFirebase(emailT,passwordT)
                storeUserData(user)

            }
            else{
                Toast.makeText(this
                    ,"Please fill all the fields"
                    ,Toast.LENGTH_SHORT)
                .show()
            }
        }
        binding.LoginGo.setOnClickListener {
            val intent = Intent(this, loginpage::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun storeUserData(user: loginDetails) {
        reference.child(user.username).setValue(user)
            .addOnSuccessListener {
                Toast.makeText(applicationContext
                    ,"Successfully registered"
                    ,Toast.LENGTH_SHORT
                )
                    .show()
                val intent = Intent(applicationContext, loginpage::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext
                    ,"Failed to register"
                    ,Toast.LENGTH_SHORT
                )
                    .show()
            }
    }

    private fun createAccountWithFirebase(emailT: String, passwordT: String) {
        authenticator.createUserWithEmailAndPassword(emailT,passwordT)
            .addOnSuccessListener {
                Toast.makeText(applicationContext
                    ,"User created successfully"
                    ,Toast.LENGTH_SHORT
                )
                    .show()
                finish()
            }.addOnFailureListener {
                Toast.makeText(applicationContext
                    ,"Create account failed"
                    ,Toast.LENGTH_SHORT
                )
                    .show()
            }
    }

}