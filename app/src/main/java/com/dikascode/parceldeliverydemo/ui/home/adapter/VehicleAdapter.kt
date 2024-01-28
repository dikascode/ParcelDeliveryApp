package com.dikascode.parceldeliverydemo.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dikascode.parceldeliverydemo.R
import com.dikascode.parceldeliverydemo.model.Vehicle

class VehicleAdapter(private val vehicles: List<Vehicle>) : RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {

    class VehicleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val typeText: TextView = view.findViewById(R.id.vehicleTypeText)
        val mottoText: TextView = view.findViewById(R.id.vehicleMottoText)
        val imageView: ImageView = view.findViewById(R.id.vehicleImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vehicle, parent, false)
        return VehicleViewHolder(view)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val vehicle = vehicles[position]
        holder.typeText.text = vehicle.type
        holder.mottoText.text = vehicle.motto
        holder.imageView.setImageResource(vehicle.imageResId)
    }

    override fun getItemCount(): Int = vehicles.size
}