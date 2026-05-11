package com.example.powertrack_app.data.remote.entity

import com.google.gson.annotations.SerializedName

data class OpenFoodFactsSearchResponse(
    val products: List<OpenFoodFactsProduct>
)

data class OpenFoodFactsProduct(
    @SerializedName("product_name") val productName: String?,
    @SerializedName("image_url") val imageUrl: String?
)