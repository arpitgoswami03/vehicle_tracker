package com.example.vehicletracking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vehicletracking.R
import com.example.vehicletracking.data_class.carDetails
import com.example.vehicletracking.databinding.CarlistRvBinding

class garage_car_adapter(private val carList: List<carDetails>) :
    RecyclerView.Adapter<garage_car_adapter.MyViewHolder>() {

    class MyViewHolder(private val binding: CarlistRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val carImage = binding.carImage
        val carName = binding.carName
        val carNum = binding.carNum
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CarlistRvBinding.inflate(
             LayoutInflater.from(parent.context)
            , parent
            , false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return carList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val car = carList[position]
        Glide.with(holder.carImage)
            .load(car.imageUrl)
            .into(holder.carImage)
        holder.carName.text = "Vehicle name: ${car.name} ${car.model}"
        holder.carNum.text = "Vehicle no.: ${car.number}"
    }

}