package com.example.powertrack_app.domain.usecase.gym

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.GymRepository
import com.example.powertrack_app.data.remote.entity.EntrenamientoEntity
import com.example.powertrack_app.domain.model.Entrenamiento
import javax.inject.Inject

class SaveEntrenamientoUseCase @Inject constructor(
    private val gymRepository: GymRepository
) {
    suspend operator fun invoke(entrenamiento: EntrenamientoEntity): NetworkResult<Entrenamiento> {
        return gymRepository.saveEntrenamiento(entrenamiento)
    }
}

