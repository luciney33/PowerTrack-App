package com.example.powertrack_app.domain.usecase.gym

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.GymRepository
import com.example.powertrack_app.domain.model.Entrenamiento
import javax.inject.Inject

class GetEntrenamientosUseCase @Inject constructor(
    private val gymRepository: GymRepository
) {
    suspend operator fun invoke(): NetworkResult<List<Entrenamiento>> {
        return gymRepository.getEntrenamientos()
    }
}

