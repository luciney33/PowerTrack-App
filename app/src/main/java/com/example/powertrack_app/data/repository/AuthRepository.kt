package com.example.powertrack_app.data.repository

import com.example.powertrack_app.common.Constantes
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.local.TokenManager
import com.example.powertrack_app.data.remote.api.GymApiService
import com.example.powertrack_app.data.remote.entity.LoginRequest
import com.example.powertrack_app.data.remote.entity.LoginResponse
import com.example.powertrack_app.data.remote.entity.PerfilRequestEntity
import com.example.powertrack_app.data.remote.entity.UsuarioEntity
import com.example.powertrack_app.data.remote.entity.toDomain
import com.example.powertrack_app.domain.model.PerfilRequest
import com.example.powertrack_app.domain.model.Usuario
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: GymApiService,
    private val tokenManager: TokenManager
) {

    suspend fun login(request: LoginRequest): NetworkResult<LoginResponse> {
        return try {
            val response = apiService.login(request)
            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                tokenManager.saveAccessToken(loginResponse.accessToken)
                tokenManager.saveRefreshToken(loginResponse.refreshToken)
                NetworkResult.Success(loginResponse)
            } else {
                NetworkResult.Error(Constantes.ERROR_USUARIO_PASSWORD_INCORRECTOS)
            }
        } catch (e: Exception) {
            NetworkResult.Error("${Constantes.ERROR_DE_RED}${e.message}")
        }
    }

    suspend fun register(usuario: UsuarioEntity): NetworkResult<Usuario> {
        return try {
            val response = apiService.register(usuario)
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!.toDomain())
            } else {
                NetworkResult.Error("${Constantes.ERROR_DEL_SERVIDOR}${response.code()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error("${Constantes.ERROR_FALLO_CONEXION}${e.message}")
        }
    }

    fun logout() {
        tokenManager.clearTokens()
    }

    suspend fun completarPerfil(perfil: PerfilRequest): NetworkResult<Usuario> {
        return try {
            val entity = PerfilRequestEntity(
                genero = perfil.genero,
                edad = perfil.edad,
                objetivo = perfil.objetivo,
                nivel = perfil.nivel,
                diasEntrenamiento = perfil.diasEntrenamiento,
                lesion = perfil.lesion,
                preferencia = perfil.preferencia,
                pesoCat = perfil.pesoCat
            )
            val response = apiService.completarPerfil(entity)
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!.toDomain())
            } else {
                NetworkResult.Error("${Constantes.ERROR_COMPLETAR_PERFIL}${response.code()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error("${Constantes.ERROR_DE_RED}${e.message}")
        }
    }

    suspend fun getPerfil(): NetworkResult<Usuario> {
        return try {
            val response = apiService.getPerfil()
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!.toDomain())
            } else {
                NetworkResult.Error(Constantes.ERROR_OBTENER_PERFIL)
            }
        } catch (e: Exception) {
            NetworkResult.Error("${Constantes.ERROR_DE_RED}${e.message}")
        }
    }
}