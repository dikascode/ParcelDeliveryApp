package com.dikascode.parceldeliverydemo.ui.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dikascode.parceldeliverydemo.R
import com.dikascode.parceldeliverydemo.model.ShipmentItem

class SearchResultAdapter (private val dataSet: MutableList<ShipmentItem>) :
    RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemTitle: TextView = view.findViewById(R.id.itemTitle)
        val trackingNumber: TextView = view.findViewById(R.id.trackingNumber)
        val routeInfo: TextView = view.findViewById(R.id.routeInfo)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.search_result_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = dataSet[position]
        viewHolder.itemTitle.text = item.title
        viewHolder.trackingNumber.text = item.trackingNumber
        viewHolder.routeInfo.text = item.routeInfo

        viewHolder.itemView.apply {
            alpha = 0f
            translationY = 50f // Starts 50 pixels below the final position
            animate().alpha(1.0f).translationY(0f).setDuration(300).start()
        }
    }

    override fun getItemCount() = dataSet.size

    fun updateData(newData: List<ShipmentItem>) {
        dataSet.clear()
        dataSet.addAll(newData)
        notifyDataSetChanged()
    }
}