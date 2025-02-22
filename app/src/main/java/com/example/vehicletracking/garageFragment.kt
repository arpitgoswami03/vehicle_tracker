package com.example.vehicletracking

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vehicletracking.adapter.garage_car_adapter
import com.example.vehicletracking.data_class.carDetails
import com.example.vehicletracking.databinding.FragmentGarageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class garageFragment : Fragment() {
    private var addBtn = true
    private lateinit var binding: FragmentGarageBinding
    private val firebaseStorage = FirebaseStorage.getInstance().getReference("carimage")
    private val firebaseDB = FirebaseDatabase.getInstance().getReference("Users")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentGarageBinding.inflate(layoutInflater)
        binding.addcarBtn.setOnClickListener {
            binding.addcarBtn.visibility = View.GONE
            setFragment(addcarFragment())
        }
        binding.addcarBtn.bringToFront()
        setUpCarList()
        return binding.root
    }

    private fun setUpCarList() {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email.toString()
        getUsername(userEmail) { username ->
            if (username != null) {
                firebaseDB.child(username).child("Car Available")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val carList = snapshot.children.mapNotNull {
                                it.getValue(carDetails::class.java)
                            }.toMutableList()
                            setRecyclerView(carList)
                        }
                        override fun onCancelled(error: DatabaseError) {
                            Log.e("FirebaseError", "Database error: ${error.message}")
                        }

                    })

            }
        }
    }
    
    private fun setRecyclerView(carList : List<carDetails>) {
        val adapter = garage_car_adapter(carList)
        binding.recyclerView.layoutManager = LinearLayoutManager(
            requireContext()
            ,LinearLayoutManager.HORIZONTAL
            ,false
        )
        binding.recyclerView.adapter = adapter
    }
    
    private fun setFragment(fragment: Fragment){
        parentFragmentManager.beginTransaction()
            .replace(R.id.homefrag,fragment)
            .commit()
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