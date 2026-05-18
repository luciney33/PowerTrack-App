package com.example.powertrack_app.data.repository

import com.example.powertrack_app.common.Constantes
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.remote.api.GymApiService
import com.example.powertrack_app.data.remote.entity.toDomain
import com.example.powertrack_app.domain.model.PlanNutricional
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NutritionRepository @Inject constructor(
    private val apiService: GymApiService
) {

    suspend fun getPlanById(id: Long): NetworkResult<PlanNutricional> {
        return try {
            val response = apiService.getPlanById(id)
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!.toDomain())
            } else {
                NetworkResult.Error(Constantes.ERROR_PLAN_NO_ENCONTRADO)
            }
        } catch (e: Exception) {
            NetworkResult.Error("${Constantes.ERROR_DE_RED}${e.message}")
        }
    }

    suspend fun getPlanRecomendado(): NetworkResult<PlanNutricional> {
        return try {
            val response = apiService.getPlanRecomendado()
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!.toDomain())
            } else {
                NetworkResult.Error(Constantes.ERROR_PLAN_NUTRICIONAL)
            }
        } catch (e: Exception) {
            NetworkResult.Error("${Constantes.ERROR_DE_RED}${e.message}")
        }
    }
}