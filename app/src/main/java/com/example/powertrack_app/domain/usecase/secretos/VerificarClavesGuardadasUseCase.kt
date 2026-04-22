package com.example.powertrack_app.domain.usecase.secretos

import com.example.powertrack_app.data.security.CryptoManager
import javax.inject.Inject

class VerificarClavesGuardadasUseCase @Inject constructor(
    private val cryptoManager: CryptoManager
) {
    suspend operator fun invoke(username: String): Boolean {
        return cryptoManager.hasStoredKeys(username)
    }
}
