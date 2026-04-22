package com.example.powertrack_app.domain.usecase.secretos

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.SecretosRepository
import javax.inject.Inject

class EliminarSecretoUseCase @Inject constructor(
    private val secretosRepository: SecretosRepository
) {
    suspend operator fun invoke(secretoId: Long): NetworkResult<Unit> {
        return secretosRepository.eliminarSecreto(secretoId)
    }
}

