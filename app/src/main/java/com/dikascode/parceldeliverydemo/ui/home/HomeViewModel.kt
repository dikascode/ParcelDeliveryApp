package com.dikascode.parceldeliverydemo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dikascode.parceldeliverydemo.R
import com.dikascode.parceldeliverydemo.model.ShipmentItem

class HomeViewModel : ViewModel() {

    private val _shipmentItems = MutableLiveData<List<ShipmentItem>>()
    val shipmentItems: LiveData<List<ShipmentItem>> = _shipmentItems

    init {
        mockShipmentItems()
    }

    private fun mockShipmentItems() {
        _shipmentItems.value = listOf(
            ShipmentItem(
                statusLabel = "Delivered",
                title = "Macbook pro M2",
                details = "This is the package details",
                price = "$1500",
                date = "2024-01-30",
                trackingNumber = "#NE43857340857904",
                routeInfo = "Paris → Morocco",
                packageImage = R.drawable.ic_profile_picture
            ),
            ShipmentItem(
                statusLabel = "In Transit",
                title = "Summer linen jacket",
                details = "Light and comfortable for summer days",
                price = "$120",
                date = "2024-01-25",
                trackingNumber = "#NEJ20089934122231",
                routeInfo = "Barcelona → Paris",
                packageImage = R.drawable.ic_profile_picture
            ),
            ShipmentItem(
                statusLabel = "Pending",
                title = "Tapered-fit jeans AW",
                details = "Stylish autumn wear jeans",
                price = "$80",
                date = "2024-01-22",
                trackingNumber = "#NEJ35870264978659",
                routeInfo = "Colombia → Paris",
                packageImage = R.drawable.ic_profile_picture
            ),
            ShipmentItem(
                statusLabel = "Awaiting Pickup",
                title = "Slim fit jeans AW",
                details = "Perfect fit with durable material",
                price = "$85",
                date = "2024-01-20",
                trackingNumber = "#NEJ35870264978659",
                routeInfo = "Bogota → Dhaka",
                packageImage = R.drawable.ic_profile_picture
            ),
            ShipmentItem(
                statusLabel = "Delivered",
                title = "Office setup desk",
                details = "Ergonomic office desk with oak finish",
                price = "$300",
                date = "2024-01-18",
                trackingNumber = "#NEJ23481570754963",
                routeInfo = "France → Germany",
                packageImage = R.drawable.ic_profile_picture
            )
        )
    }

}