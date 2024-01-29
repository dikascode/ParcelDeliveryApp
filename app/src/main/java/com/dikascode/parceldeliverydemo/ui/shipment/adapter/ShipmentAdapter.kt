package com.dikascode.parceldeliverydemo.ui.shipment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dikascode.parceldeliverydemo.R
import com.dikascode.parceldeliverydemo.model.ShipmentItem

class ShipmentAdapter(private val shipmentItems: MutableList<ShipmentItem>) :
    RecyclerView.Adapter<ShipmentAdapter.ShipmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShipmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.shipment_item, parent, false)
        return ShipmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShipmentViewHolder, position: Int) {
        val shipment = shipmentItems[position]
        holder.bind(shipment)
    }

    override fun getItemCount(): Int {
        return shipmentItems.size
    }

    fun updateData(newShipmentItems: List<ShipmentItem>) {
        this.shipmentItems.clear()
        this.shipmentItems.addAll(newShipmentItems)
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

        fun bind(shipmentItem: ShipmentItem) {
            statusLabel.text = shipmentItem.statusLabel
            shipmentTitle.text = shipmentItem.title
            shipmentDetails.text = shipmentItem.details
            shipmentPrice.text = shipmentItem.price
            shipmentDate.text = shipmentItem.date


            statusIcon.setImageResource(shipmentItem.statusIcon)
            shipmentPackageImage.setImageResource(shipmentItem.packageImage)

            val context = itemView.context
            val color = when (shipmentItem.statusLabel) {
                "in-progress" -> context.getColor(R.color.status_green)
                "pending" -> context.getColor(R.color.orange)
                "loading" -> context.getColor(R.color.blue)
                else -> context.getColor(R.color.orange)
            }

            when (shipmentItem.statusLabel) {
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
