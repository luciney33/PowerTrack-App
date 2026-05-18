package com.example.powertrack_app.domain.usecase.gym

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.AuthRepository
import com.example.powertrack_app.domain.model.PerfilRequest
import com.example.powertrack_app.domain.model.Usuario
import javax.inject.Inject

class CompletarPerfilUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(perfil: PerfilRequest): NetworkResult<Usuario> {
        return repository.completarPerfil(perfil)
    }
}