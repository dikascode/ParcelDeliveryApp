package com.dikascode.parceldeliverydemo.ui.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dikascode.parceldeliverydemo.R
import com.dikascode.parceldeliverydemo.model.ShipmentItem

class SearchResultAdapter (private val dataSet: List<ShipmentItem>) :
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
    }

    override fun getItemCount() = dataSet.size
}