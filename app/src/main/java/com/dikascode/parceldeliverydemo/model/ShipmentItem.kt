package com.dikascode.parceldeliverydemo.model

import com.dikascode.parceldeliverydemo.R

data class ShipmentItem(
    val statusIcon: Int = R.drawable.ic_notifications_black_24dp,
    val statusLabel: String,
    val title: String,
    val details: String,
    val price: String,
    val date: String,
    val trackingNumber: String = "#NEJ20089934122231",
    val routeInfo: String = "Barcelona → Paris",
    val packageImage: Int
)

