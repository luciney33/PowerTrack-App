package com.example.powertrack_app.domain.usecase

import com.example.powertrack_app.data.repository.GymRepository
import com.example.powertrack_app.data.security.CryptoManager
import com.example.powertrack_app.data.security.SessionManager
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val gymRepository: GymRepository,
    private val cryptoManager: CryptoManager,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke() {
        sessionManager.getCurrentUser()?.username?.let {
            gymRepository.logout()
            cryptoManager.clearAllKeys(it)
            sessionManager.clearSession()
        }
    }
}
