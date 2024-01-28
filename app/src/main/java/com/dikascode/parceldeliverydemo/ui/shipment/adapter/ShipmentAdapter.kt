package com.dikascode.parceldeliverydemo.ui.shipment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dikascode.parceldeliverydemo.R
import com.dikascode.parceldeliverydemo.model.Shipment

class ShipmentAdapter(private val shipments: MutableList<Shipment>) :
    RecyclerView.Adapter<ShipmentAdapter.ShipmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShipmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.shipment_item, parent, false)
        return ShipmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShipmentViewHolder, position: Int) {
        val shipment = shipments[position]
        holder.bind(shipment)
    }

    override fun getItemCount(): Int {
        return shipments.size
    }

    fun updateData(newShipments: List<Shipment>) {
        this.shipments.clear()
        this.shipments.addAll(newShipments)
        notifyDataSetChanged()
    }

    class ShipmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val statusIcon: ImageView = itemView.findViewById(R.id.statusIcon)
        private val statusLabel: TextView = itemView.findViewById(R.id.statusLabel)
        private val shipmentTitle: TextView = itemView.findViewById(R.id.shipmentTitle)
        private val shipmentDetails: TextView = itemView.findViewById(R.id.shipmentDetails)
        private val shipmentPrice: TextView = itemView.findViewById(R.id.shipmentPrice)
        private val shipmentDate: TextView = itemView.findViewById(R.id.shipmentDate)
        private val shipmentPackageImage: ImageView = itemView.findViewById(R.id.shipmentPackageImage)

        fun bind(shipment: Shipment) {
            statusLabel.text = shipment.statusLabel
            shipmentTitle.text = shipment.title
            shipmentDetails.text = shipment.details
            shipmentPrice.text = shipment.price
            shipmentDate.text = shipment.date


            statusIcon.setImageResource(shipment.statusIcon)
            shipmentPackageImage.setImageResource(shipment.packageImage)

            val context = itemView.context
            val color = when (shipment.statusLabel) {
                "in-progress" -> context.getColor(R.color.status_green)
                "pending" -> context.getColor(R.color.orange)
                "loading" -> context.getColor(R.color.blue)
                else -> context.getColor(R.color.orange)
            }

            when (shipment.statusLabel) {
                "loading" -> {
                    statusIcon.setImageResource(R.drawable.loading)
                    statusLabel.setTextColor(ContextCompat.getColor(itemView.context, R.color.blue))
                }
                "in-progress" -> {
                    statusIcon.setImageResource(R.drawable.in_progress)
                    statusLabel.setTextColor(ContextCompat.getColor(itemView.context, R.color.status_green))
                }
                "pending" -> {
                    statusIcon.setImageResource(R.drawable.pending)
                    statusLabel.setTextColor(ContextCompat.getColor(itemView.context, R.color.orange))
                }
            }
            statusLabel.setTextColor(color)
        }
    }
}
