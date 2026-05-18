package com.example.powertrack_app.domain.usecase

import com.example.powertrack_app.common.Constantes
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.AuthRepository
import com.example.powertrack_app.data.remote.entity.UsuarioEntity
import com.example.powertrack_app.domain.model.Usuario
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val gymRepository: AuthRepository
) {
    suspend operator fun invoke(
        username: String,
        email: String,
        nombre: String,
        password: String
    ): NetworkResult<Usuario> {
        if (password.length < 8) {
            return NetworkResult.Error(Constantes.ERROR_PASSWORD_MINIMA)
        }
        val usuarioEntity = UsuarioEntity(
            id = 0,
            username = username,
            email = email,
            nombre = nombre,
            password = password
        )
        return gymRepository.register(usuarioEntity)
    }
}
