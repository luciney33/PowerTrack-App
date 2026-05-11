package com.example.powertrack_app.domain.usecase.gym

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.OpenFoodFactsRepository
import javax.inject.Inject

class GetComidaFotoUseCase @Inject constructor(
    private val repository: OpenFoodFactsRepository
) {
    suspend operator fun invoke(nombre: String): NetworkResult<String> = repository.getFotoUrl(nombre)
}