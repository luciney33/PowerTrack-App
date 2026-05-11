package com.example.powertrack_app.domain.usecase.gym

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.ExerciseDbRepository
import javax.inject.Inject

class GetEjercicioGifUseCase @Inject constructor(
    private val repository: ExerciseDbRepository
) {
    suspend operator fun invoke(nombre: String): NetworkResult<String> = repository.getGifUrl(nombre)
}