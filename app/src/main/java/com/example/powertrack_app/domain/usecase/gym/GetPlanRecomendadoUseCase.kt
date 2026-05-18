package com.example.powertrack_app.domain.usecase.gym

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.NutritionRepository
import com.example.powertrack_app.domain.model.PlanNutricional
import javax.inject.Inject

class GetPlanRecomendadoUseCase @Inject constructor(
    private val repository: NutritionRepository
) {
    suspend operator fun invoke(): NetworkResult<PlanNutricional> {
        return repository.getPlanRecomendado()
    }
}