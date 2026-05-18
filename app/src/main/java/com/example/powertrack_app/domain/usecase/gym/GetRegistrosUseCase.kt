package com.example.powertrack_app.domain.usecase.gym

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.remote.entity.RegistroEntrenamientoResponseEntity
import com.example.powertrack_app.data.repository.TrainingRepository
import javax.inject.Inject

class GetRegistrosUseCase @Inject constructor(
    private val repository: TrainingRepository
) {
    suspend operator fun invoke(): NetworkResult<List<RegistroEntrenamientoResponseEntity>> {
        return repository.getRegistros()
    }
}