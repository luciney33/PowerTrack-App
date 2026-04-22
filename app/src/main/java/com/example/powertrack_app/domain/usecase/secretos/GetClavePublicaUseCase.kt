package com.example.powertrack_app.domain.usecase.secretos

import com.example.powertrack_app.common.Constantes
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.security.CryptoManager
import javax.inject.Inject

class GetClavePublicaUseCase @Inject constructor(
    private val cryptoManager: CryptoManager
) {
    suspend operator fun invoke(): NetworkResult<String> {
        return try {
            val clavePublicaBase64 = cryptoManager.getPublicKeyBase64()
            NetworkResult.Success(clavePublicaBase64)
        } catch (e: Exception) {
            NetworkResult.Error("${Constantes.ERROR_OBTENER_CLAVE_PUBLICA}${e.message}")
        }
    }
}

