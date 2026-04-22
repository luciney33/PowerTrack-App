package com.example.powertrack_app.domain.usecase.secretos

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.SecretosRepository
import com.example.powertrack_app.domain.model.SecretoDescifrado
import javax.inject.Inject

class DescifrarSecretoUseCase @Inject constructor(
    private val secretosRepository: SecretosRepository
) {
    suspend operator fun invoke(
        secretoId: Long,
        passwordUsuario: String,
        autorId: Long? = null
    ): NetworkResult<SecretoDescifrado> {
        return secretosRepository.descifrarSecreto(
            secretoId = secretoId,
            passwordUsuario = passwordUsuario,
            autorId = autorId
        )
    }
}
