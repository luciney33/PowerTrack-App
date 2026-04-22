package com.example.powertrack_app.domain.usecase.secretos

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.SecretosRepository
import com.example.powertrack_app.domain.model.Secreto
import javax.inject.Inject

class CrearSecretoUseCase @Inject constructor(
    private val secretosRepository: SecretosRepository
) {
    suspend operator fun invoke(
        contenidoPlano: String,
        passwordUsuario: String,
        receptorId: Long? = null
    ): NetworkResult<Secreto> {
        val crearResult = secretosRepository.crearSecreto(
            contenidoPlano = contenidoPlano,
            passwordUsuario = passwordUsuario
        )

        return when (crearResult) {
            is NetworkResult.Success -> {
                val secretoCreado = crearResult.data

                if (receptorId != null) {
                    val compartirResult = secretosRepository.compartirSecreto(
                        secretoId = secretoCreado.id,
                        receptorId = receptorId,
                        passwordUsuario = passwordUsuario
                    )

                    when (compartirResult) {
                        is NetworkResult.Success -> NetworkResult.Success(secretoCreado)
                        is NetworkResult.Error -> NetworkResult.Error("Secreto creado pero error al compartir: ${compartirResult.message}")
                    }
                } else {
                    NetworkResult.Success(secretoCreado)
                }
            }
            is NetworkResult.Error -> crearResult
        }
    }
}
