package com.example.powertrack_app.domain.usecase

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.AuthRepository
import com.example.powertrack_app.data.remote.entity.LoginRequest
import com.example.powertrack_app.data.remote.entity.LoginResponse
import com.example.powertrack_app.data.security.SessionManager
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val gymRepository: AuthRepository,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(username: String, password: String): NetworkResult<LoginResponse> {
        val result = gymRepository.login(LoginRequest(username, password))
        if (result is NetworkResult.Success) {
            sessionManager.setCurrentUser(result.data.usuario)
        }
        return result
    }
}
