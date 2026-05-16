package com.example.powertrack_app.domain.usecase

import com.example.powertrack_app.data.repository.GymRepository
import com.example.powertrack_app.data.security.SessionManager
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val gymRepository: GymRepository,
    private val sessionManager: SessionManager
) {
    operator fun invoke() {
        gymRepository.logout()
        sessionManager.clearSession()
    }
}
