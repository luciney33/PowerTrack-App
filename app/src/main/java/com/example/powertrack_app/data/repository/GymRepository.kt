package com.example.powertrack_app.data.repository

import com.example.powertrack_app.common.Constantes
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.local.TokenManager
import com.example.powertrack_app.data.remote.api.GymApiService
import com.example.powertrack_app.data.remote.entity.EntrenamientoEntity
import com.example.powertrack_app.data.remote.entity.LoginRequest
import com.example.powertrack_app.data.remote.entity.LoginResponse
import com.example.powertrack_app.data.remote.entity.PerfilRequestEntity
import com.example.powertrack_app.data.remote.entity.UpdatePublicKeyRequest
import com.example.powertrack_app.data.remote.entity.UsuarioEntity
import com.example.powertrack_app.data.remote.entity.toDomain
import com.example.powertrack_app.domain.model.Ejercicio
import com.example.powertrack_app.domain.model.Entrenamiento
import com.example.powertrack_app.domain.model.PerfilRequest
import com.example.powertrack_app.domain.model.Usuario
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GymRepository @Inject constructor(
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

    suspend fun getEntrenamientos(): NetworkResult<List<Entrenamiento>> {
        return try {
            val response = apiService.getEntrenamientos()
            if (response.isSuccessful) {
                val domainList = response.body()?.map { it.toDomain() } ?: emptyList()
                NetworkResult.Success(domainList)
            } else {
                NetworkResult.Error(Constantes.ERROR_NO_CARGAR_ENTRENAMIENTOS)
            }
        } catch (e: Exception) {
            NetworkResult.Error("${Constantes.ERROR_GENERICO}${e.message}")
        }
    }

    suspend fun getEntrenamientoById(id: Long): NetworkResult<Entrenamiento> {
        return try {
            val response = apiService.getEntrenamientoById(id)
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!.toDomain())
            } else {
                NetworkResult.Error(Constantes.ERROR_ENTRENAMIENTO_NO_ENCONTRADO)
            }
        } catch (e: Exception) {
            NetworkResult.Error(Constantes.ERROR_BUSCAR_DETALLE)
        }
    }

    suspend fun saveEntrenamiento(entrenamiento: EntrenamientoEntity): NetworkResult<Entrenamiento> {
        return try {
            val response = if (entrenamiento.id == 0L) {
                apiService.createEntrenamiento(entrenamiento)
            } else {
                apiService.updateEntrenamiento(entrenamiento.id, entrenamiento)
            }

            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!.toDomain())
            } else {
                NetworkResult.Error(Constantes.ERROR_GUARDAR_SERVIDOR)
            }
        } catch (e: Exception) {
            NetworkResult.Error(Constantes.ERROR_FALLO_RED)
        }
    }

    suspend fun deleteEntrenamiento(id: Long): NetworkResult<Unit> {
        return try {
            val response = apiService.deleteEntrenamiento(id)
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error(Constantes.ERROR_NO_ELIMINAR)
            }
        } catch (e: Exception) {
            NetworkResult.Error(Constantes.ERROR_DE_CONEXION)
        }
    }

    suspend fun getEjercicios(): NetworkResult<List<Ejercicio>> {
        return try {
            val response = apiService.getEjercicios()
            if (response.isSuccessful) {
                val list = response.body()?.map { it.toDomain() } ?: emptyList()
                NetworkResult.Success(list)
            } else {
                NetworkResult.Error(Constantes.ERROR_CARGAR_EJERCICIOS)
            }
        } catch (e: Exception) {
            NetworkResult.Error("${Constantes.ERROR_GENERICO}${e.message}")
        }
    }

    suspend fun updatePublicKey(publicKeyBase64: String): NetworkResult<String> {
        return try {
            val request = UpdatePublicKeyRequest(publicKeyBase64)
            val response = apiService.updatePublicKey(request)
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error("${Constantes.ERROR_ACTUALIZAR_CLAVE_PUBLICA}${response.code()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error("${Constantes.ERROR_RED_ACTUALIZAR_CLAVE_PUBLICA}${e.message}")
        }
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
                preferencia = perfil.preferencia
            )
            val response = apiService.completarPerfil(entity)
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!.toDomain())
            } else {
                NetworkResult.Error("Error al completar perfil: ${response.code()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Error de red: ${e.message}")
        }
    }

    suspend fun getPerfil(): NetworkResult<Usuario> {
        return try {
            val response = apiService.getPerfil()
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!.toDomain())
            } else {
                NetworkResult.Error("Error al obtener perfil")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Error de red: ${e.message}")
        }
    }

}