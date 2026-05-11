package com.example.powertrack_app.data.repository

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.remote.api.OpenFoodFactsApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OpenFoodFactsRepository @Inject constructor(
    private val apiService: OpenFoodFactsApiService
) {
    suspend fun getFotoUrl(nombre: String): NetworkResult<String> {
        return try {
            val response = apiService.searchFood(nombre)
            if (response.isSuccessful) {
                val imageUrl = response.body()?.products?.firstOrNull()?.imageUrl
                if (imageUrl != null) {
                    NetworkResult.Success(imageUrl)
                } else {
                    NetworkResult.Error("Imagen no encontrada para \"$nombre\"")
                }
            } else {
                NetworkResult.Error("Error ${response.code()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Error de red: ${e.message}")
        }
    }
}