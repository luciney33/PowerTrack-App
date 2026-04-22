package com.example.powertrack_app.data.remote.entity

import com.google.gson.annotations.SerializedName

data class UpdatePublicKeyRequest(
    @SerializedName("publicKeyBase64")
    val publicKeyBase64: String
)

