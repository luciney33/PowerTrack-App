package com.example.powertrack_app.domain.usecase.secretos

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.SecretosRepository
import com.example.powertrack_app.domain.model.Secreto
import javax.inject.Inject

class ObtenerSecretosUseCase @Inject constructor(
    private val secretosRepository: SecretosRepository
) {
    suspend operator fun invoke(): NetworkResult<List<Secreto>> {
        return secretosRepository.getSecretos()
    }
}

