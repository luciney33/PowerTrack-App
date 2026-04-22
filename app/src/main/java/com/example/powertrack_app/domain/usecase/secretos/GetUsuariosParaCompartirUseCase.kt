package com.example.powertrack_app.domain.usecase.secretos

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.SecretosRepository
import com.example.powertrack_app.domain.model.Usuario
import javax.inject.Inject

class GetUsuariosParaCompartirUseCase @Inject constructor(
    private val secretosRepository: SecretosRepository
) {
    suspend operator fun invoke(): NetworkResult<List<Usuario>> {
        return secretosRepository.getUsuarios()
    }
}

