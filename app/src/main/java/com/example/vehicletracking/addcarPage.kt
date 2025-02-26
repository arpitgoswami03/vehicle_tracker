package com.example.vehicletracking

import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.vehicletracking.data_class.carDetails
import com.example.vehicletracking.databinding.ActivityAddcarBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class addcarPage : AppCompatActivity() {
    private lateinit var binding: ActivityAddcarBinding
    private val READ_STORAGE_CODE = 100
    private var imageUri: Uri? = null
    private var firebaseDB: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
    private var firebaseStorage: StorageReference =
        FirebaseStorage.getInstance().getReference("carimage")
    private val firebaseAuthenticator = FirebaseAuth.getInstance()

    // Take image from gallery (2)
    private val startActivityForResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            Log.i("gallery", "Result Code: ${result.resultCode}")
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                imageUri = data?.data
                if (imageUri != null) {
                    binding.carImage.setImageURI(imageUri)
                } else {
                    Log.e("gallery", "Image URI is null")
                }
            } else {
                Log.e("gallery", "Image selection failed or canceled")
            }
        }


    // Main function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddcarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fill the details about car and handle permission (1)
        binding.addImg.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(READ_MEDIA_IMAGES),
                    READ_STORAGE_CODE
                )
            } else {
                launchGallery()
            }
        }

        // Upload image and save details (3)
        binding.addcarBtn.setOnClickListener {
            val imageT = imageUri
            val nameT = binding.vehiclename.text.toString()
            val modelT = binding.vehicleomdel.text.toString()
            val numberT = binding.carnumber.text.toString()

            if (imageUri != null
                && nameT.isNotEmpty()
                && modelT.isNotEmpty()
                && numberT.isNotEmpty()
            ) {
                uploadImageAndSaveDetails(nameT, modelT, numberT, imageT!!)
                finish()
            } else {
                Toast.makeText(
                    this, "Please fill all the Details", Toast.LENGTH_SHORT
                ).show()
                Log.e("UploadProcess", "Text field might be empty")
            }
        }

        binding.cancelBtn.setOnClickListener {
            val intent = Intent(this, homescreen::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Find reference to the firestorage (4)
    private fun uploadImageAndSaveDetails(
        name: String,
        model: String,
        number: String,
        imageUri: Uri,
    ) {

        firebaseStorage.child(number).putFile(imageUri)
            .addOnSuccessListener { task ->
                Log.d("storageImg", "Image upload successful.")
                task.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { url ->
                        Log.d("storageImg", "Download URL retrieved: $url")
                        val imageUrl = url.toString()
                        saveCarDetails(name, model, number, imageUrl)
                    }
                    .addOnFailureListener { exception ->
                        Log.e("storageImg", "${exception.message}")
                    }
            }
            .addOnFailureListener { exception ->
                Log.e("UploadProcess", "Image upload failed: ${exception.message}")
            }
    }


    // Upload the data to the database (5)
    private fun saveCarDetails(name: String, model: String, number: String, imageUrl: String) {
        val userEmail = firebaseAuthenticator.currentUser?.email.toString()

        if (userEmail.isNotEmpty()) {
            getUsername(userEmail) { username ->
                if (username != null) {
                    val car = carDetails(name, model, number, imageUrl)
                    firebaseDB.child(username)
                        .child("Car Available")
                        .child("$name $model")
                        .setValue(car)
                        .addOnSuccessListener {
                            Log.i("firebase", "Car details saved successfully")
                        }
                        .addOnFailureListener { exception ->
                            Log.e("firebase", "Failed to save car details: ${exception.message}")
                        }
                } else {
                    Log.e("Firebase", "Username is null, cannot save car details.")
                }
            }
        } else {
            Log.e("firebase", "User email is empty or null")
        }
    }

    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult.launch(intent)
    }

    private fun getUsername(currentUserEmail: String, callback: (String?) -> Unit) {
        firebaseDB.orderByChild("email").equalTo(currentUserEmail)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val username = snapshot.children.firstOrNull()
                        ?.child("username")
                        ?.getValue(String::class.java)
                    Log.i("Firebase", "Username found: $username")
                    callback(username)  // Pass username to callback
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Database error: ${error.message}")
                    callback(null)  // Pass null on failure
                }
            })
    }


}