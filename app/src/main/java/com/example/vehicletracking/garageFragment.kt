package com.example.vehicletracking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.vehicletracking.databinding.FragmentGarageBinding

class garageFragment : Fragment() {
    private var addBtn = true
    private lateinit var binding: FragmentGarageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentGarageBinding.inflate(layoutInflater)
        binding.floatbtn.setOnClickListener {
            val cancelID = R.drawable.delete
            val addID = R.drawable.baseline_add_24
            if (addBtn) {
                setFragment(addcarFragment())
                binding.floatbtn.setImageResource(cancelID)
                binding.floatbtn.contentDescription="Cancel"
                addBtn = false
            }
            else{
                setFragment(garageFragment())
                binding.floatbtn.setImageResource(addID)
                binding.floatbtn.contentDescription="Add Vehicle"
                addBtn = true
            }
        }
        return binding.root
    }

    private fun setFragment(fragment: Fragment){
        parentFragmentManager.beginTransaction()
            .replace(R.id.homefrag,fragment)
            .commit()
    }
}