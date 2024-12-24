package com.example.vehicletracking

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.vehicletracking.databinding.FragmentAddcarBinding

class addcarFragment : Fragment() {
    private lateinit var binding: FragmentAddcarBinding
    private val READ_STORAGE_CODE = 100
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentAddcarBinding.inflate(layoutInflater)

        binding.carImage.setOnClickListener {
            if(ContextCompat.checkSelfPermission(requireContext()
                    ,android.Manifest.permission.READ_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(
                    requireActivity()
                    ,arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    ,READ_STORAGE_CODE)
            }
            else{
                launchGalary()
            }
        }
        binding.addcarBtn.setOnClickListener {
            val image = binding.carImage.drawable
            val name = binding.vehiclename.text.toString()
            val model = binding.vehicleomdel.text.toString()
            val number = binding.carnumber.text.toString()
            if(name.isNotEmpty() && model.isNotEmpty() && number.isNotEmpty()){

            }
            else{
                Toast.makeText(
                    requireContext()
                    ,"Please fill all the Details"
                    ,Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        return binding.root
    }

    private fun launchGalary() {
        val intent = Intent(READ_STORAGE_CODE, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    }
}