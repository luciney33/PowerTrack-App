package com.example.powertrack_app.domain.usecase.gym

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.GymRepository
import javax.inject.Inject

class DeleteEntrenamientoUseCase @Inject constructor(
    private val gymRepository: GymRepository
) {
    suspend operator fun invoke(id: Long): NetworkResult<Unit> {
        return gymRepository.deleteEntrenamiento(id)
    }
}

