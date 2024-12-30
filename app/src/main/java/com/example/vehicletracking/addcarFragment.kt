package com.example.vehicletracking

import android.Manifest.permission.READ_MEDIA_IMAGES
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.vehicletracking.databinding.FragmentAddcarBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class addcarFragment : Fragment() {
    private lateinit var binding: FragmentAddcarBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val READ_STORAGE_CODE = 100
    private var imageUri: Uri? = null
    private var firebaseDB: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var firebaseRef: DatabaseReference = firebaseDB.getReference("Users")
    private var firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    private var storageRef: StorageReference = firebaseStorage.getReference("CarImage")

    // Take image from gallery (2)
    private val startActivityForResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                imageUri = data?.data
                if (imageUri != null) {
                    binding.carImage.setImageURI(imageUri)
                } else {
                    Log.i("gallery", "No image selected")
                }
            } else {
                Log.i("gallery", "Error in image selection")
            }
        }

    // Main function
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddcarBinding.inflate(layoutInflater)
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val currentUser = sharedPreferences.getString("username", "NewUser")
//        val currentUser = firebasAuth.currentUser.toString()
        Log.e("currentUser", currentUser!!)

        // Fill the details about car and handle permission (1)
        binding.addImg.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(), READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(READ_MEDIA_IMAGES), READ_STORAGE_CODE
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
            } else {
                Toast.makeText(
                    requireContext(), "Please fill all the Details", Toast.LENGTH_SHORT
                ).show()
                Log.e("UploadProcess", "Text field might be empty")
            }
        }
        return binding.root
    }

    // Find reference to the firestorage (4)
    private fun uploadImageAndSaveDetails(
        name: String,
        model: String,
        number: String,
        imageUri: Uri,
    ) {

        storageRef.child(number).putFile(imageUri)
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
    private fun saveCarDetails(
        name: String,
        model: String,
        number: String,
        imageUrl: String,
    ) {

        val currentUser = sharedPreferences.getString("username", "NewUser")
//        val currentUser = firebasAuth.currentUser.toString()
        Log.e("currentUser", currentUser!!)
        if (currentUser.isNotEmpty()) {
            val car = carDetails(name, model, number, imageUrl)
            firebaseRef.child(currentUser).child("$name $model").setValue(car)
                .addOnSuccessListener {
                    Log.i("firebase", "Car details saved successfully")
                    setFragment(garageFragment())
                }
                .addOnFailureListener { exception ->
                    Log.e("firebase", "Failed to save car details: ${exception.message}")
                }
        } else {
            Log.e("firebase", "$currentUser might not exist")
        }
    }

    private fun setFragment(fragment: Fragment){
        parentFragmentManager.beginTransaction()
            .replace(R.id.homefrag,fragment)
            .commit()
    }

    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult.launch(intent)
    }
}