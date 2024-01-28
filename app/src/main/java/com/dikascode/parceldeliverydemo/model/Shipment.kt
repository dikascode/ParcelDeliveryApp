package com.dikascode.parceldeliverydemo.model

import com.dikascode.parceldeliverydemo.R

data class Shipment(
    val statusIcon: Int = R.drawable.ic_notifications,
    val statusLabel: String,
    val title: String,
    val details: String,
    val price: String,
    val date: String,
    val packageImage: Int
)

