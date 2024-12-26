package com.example.moneysnap.data.map

import com.google.gson.annotations.SerializedName

data class Root(
    val items: List<Place>,
    @SerializedName("total")
    val totalResults: Int
)

data class Place(
    @SerializedName("title")
    val title: String, // Place name
    @SerializedName("category")
    val category: String, // Category (e.g., Bank, ATM)
    @SerializedName("address")
    val address: String, // Address
    @SerializedName("roadAddress")
    val roadAddress: String, // Road Address
    @SerializedName("mapx")
    val mapX: Double, // X Coordinate (longitude)
    @SerializedName("mapy")
    val mapY: Double // Y Coordinate (latitude)
)