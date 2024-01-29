package com.dikascode.parceldeliverydemo.ui.shipment


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.dikascode.parceldeliverydemo.R
import com.dikascode.parceldeliverydemo.model.ShipmentItem


class ShipmentViewModel : ViewModel() {
    private val _shipments = MutableLiveData<List<ShipmentItem>>()
    val shipments: LiveData<List<ShipmentItem>> = _shipments

    val tabCounts: LiveData<Map<String, Int>> = shipments.map { list ->
        list.groupingBy { it.statusLabel }.eachCount()
    }

    init {
        _shipments.value = getShipments()
    }

    fun filterShipments(statusFilter: String) {
        _shipments.value = if (statusFilter.isEmpty()) {
            getShipments()
        } else {
            getShipments().filter { it.statusLabel == statusFilter }
        }
    }


    private fun getShipments(): List<ShipmentItem> {
        val mockList = mutableListOf<ShipmentItem>()
        val statuses = mutableListOf<String>().apply {
            addAll(List(5) { "loading" })
            addAll(List(3) { "in-progress" })
            addAll(List(4) { "pending" })
        }

        statuses.shuffle()
        statuses.forEachIndexed { _, status ->
            val shipmentItem = ShipmentItem(
                statusLabel = status,
                title = "Arriving today!",
                details = "Your delivery, #NEJ20089934122231 from Atlanta, is arriving today!",
                price = "$1400 USD",
                date = "Sep 20,2023",
                packageImage = R.drawable.ic_profile_picture
            )
            mockList.add(shipmentItem)
        }
        return mockList
    }
}