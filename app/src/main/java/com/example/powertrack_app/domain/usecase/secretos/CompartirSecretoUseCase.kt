package com.example.powertrack_app.domain.usecase.secretos

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.SecretosRepository
import javax.inject.Inject

class CompartirSecretoUseCase @Inject constructor(
    private val secretosRepository: SecretosRepository
) {
    suspend operator fun invoke(
        secretoId: Long,
        receptorId: Long,
        passwordUsuario: String
    ): NetworkResult<Unit> {
        return secretosRepository.compartirSecreto(
            secretoId = secretoId,
            receptorId = receptorId,
            passwordUsuario = passwordUsuario
        )
    }
}

