package com.example.powertrack_app.domain.usecase.secretos

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.SecretosRepository
import javax.inject.Inject

class RevocarAccesoSecretoUseCase @Inject constructor(
    private val secretosRepository: SecretosRepository
) {
    suspend operator fun invoke(
        secretoId: Long,
        usuarioId: Long
    ): NetworkResult<Unit> {
        return secretosRepository.revocarAcceso(
            secretoId = secretoId,
            usuarioId = usuarioId
        )
    }
}

