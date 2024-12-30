package com.example.vehicletracking

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vehicletracking.databinding.ActivitySignuppageBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class signuppage : AppCompatActivity() {
    private lateinit var binding: ActivitySignuppageBinding
    private var database = FirebaseDatabase.getInstance()
    private var reference = database.getReference("Users")

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
                        ,Toast.LENGTH_SHORT)
                        .show()
                }
                else if(!emailT.contains("@gmail.com")){
                    Toast.makeText(
                        this
                        ,"Please enter a valid email"
                        ,Toast.LENGTH_SHORT)
                        .show()
                }
                else {
                    reference.child(usernameT).setValue(user).addOnSuccessListener {
                        Toast.makeText(this, "Successfully registered", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(this, loginpage::class.java)
                        startActivity(intent)
                        finish()
                    }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to register", Toast.LENGTH_SHORT)
                                .show()
                        }
//
//                    checkUsernameUnique(usernameT) { isUnique ->
//                        if (isUnique) {
//                            reference.child(usernameT).setValue(user).addOnSuccessListener {
//                                Toast.makeText(this, "Successfully registered", Toast.LENGTH_SHORT)
//                                    .show()
//                                val intent = Intent(this, loginpage::class.java)
//                                startActivity(intent)
//                                finish()
//                            }
//                            .addOnFailureListener {
//                                Toast.makeText(this, "Failed to register", Toast.LENGTH_SHORT)
//                                    .show()
//                            }
//                        } else {
//                            Toast.makeText(
//                                this,
//                                "Username already exists. Please choose another.",
//                                Toast.LENGTH_SHORT
//                            )
//                                .show()
//                        }
//                    }
                }

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

//    fun checkUsernameUnique(username: String, onResult: (Boolean) -> Unit) {
//        val databaseReference = FirebaseDatabase.getInstance().getReference("users")
//
//        if (databaseReference != null) {
//            databaseReference.child(username).addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    onResult(!snapshot.exists()) // If it exists, it's not unique, so return false.
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    println("Database error: ${error.message}")
//                    onResult(false) // Treat cancellation as not unique for safety.
//                }
//            })
//        } else {
//            println("Database reference is null.")
//            onResult(false)
//        }
//    }
}