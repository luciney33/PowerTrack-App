package com.example.powertrack_app.data.repository

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.remote.api.ExerciseDbApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseDbRepository @Inject constructor(
    private val apiService: ExerciseDbApiService
) {
    suspend fun getGifUrl(nombre: String): NetworkResult<String> {
        return try {
            val response = apiService.getExercisesByName(nombre.lowercase())
            if (response.isSuccessful) {
                val gifUrl = response.body()?.firstOrNull()?.gifUrl
                if (gifUrl != null) {
                    NetworkResult.Success(gifUrl)
                } else {
                    NetworkResult.Error("GIF no encontrado para \"$nombre\"")
                }
            } else {
                NetworkResult.Error("Error ${response.code()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Error de red: ${e.message}")
        }
    }
}