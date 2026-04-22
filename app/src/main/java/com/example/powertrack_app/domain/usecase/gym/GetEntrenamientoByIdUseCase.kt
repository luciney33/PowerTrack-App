package com.example.powertrack_app.domain.usecase.gym

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.GymRepository
import com.example.powertrack_app.domain.model.Entrenamiento
import javax.inject.Inject

class GetEntrenamientoByIdUseCase @Inject constructor(
    private val gymRepository: GymRepository
) {
    suspend operator fun invoke(id: Long): NetworkResult<Entrenamiento> {
        return gymRepository.getEntrenamientoById(id)
    }
}

