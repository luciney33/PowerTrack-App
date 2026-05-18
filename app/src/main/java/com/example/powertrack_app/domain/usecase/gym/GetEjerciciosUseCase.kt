package com.example.powertrack_app.domain.usecase.gym

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.TrainingRepository
import com.example.powertrack_app.domain.model.Ejercicio
import javax.inject.Inject

class GetEjerciciosUseCase @Inject constructor(
    private val gymRepository: TrainingRepository
) {
    suspend operator fun invoke(): NetworkResult<List<Ejercicio>> {
        return gymRepository.getEjercicios()
    }
}

