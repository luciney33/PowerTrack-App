package com.example.powertrack_app.data.repository

import com.example.powertrack_app.common.Constantes
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.remote.api.GymApiService
import com.example.powertrack_app.data.remote.entity.EntrenamientoEntity
import com.example.powertrack_app.data.remote.entity.RegistroEntrenamientoRequestEntity
import com.example.powertrack_app.data.remote.entity.RegistroEntrenamientoResponseEntity
import com.example.powertrack_app.data.remote.entity.toDomain
import com.example.powertrack_app.domain.model.Ejercicio
import com.example.powertrack_app.domain.model.Entrenamiento
import com.example.powertrack_app.domain.model.Rutina
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrainingRepository @Inject constructor(
    private val apiService: GymApiService
) {

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

    suspend fun getRutinaById(id: Long): NetworkResult<Rutina> {
        return try {
            val response = apiService.getRutinaById(id)
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!.toDomain())
            } else {
                NetworkResult.Error(Constantes.ERROR_RUTINA_NO_ENCONTRADA)
            }
        } catch (e: Exception) {
            NetworkResult.Error("${Constantes.ERROR_DE_RED}${e.message}")
        }
    }

    suspend fun getRutinaRecomendada(): NetworkResult<Rutina> {
        return try {
            val response = apiService.getRutinaRecomendada()
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!.toDomain())
            } else {
                NetworkResult.Error(Constantes.ERROR_RUTINA_RECOMENDADA)
            }
        } catch (e: Exception) {
            NetworkResult.Error("${Constantes.ERROR_DE_RED}${e.message}")
        }
    }

    suspend fun createRegistro(registro: RegistroEntrenamientoRequestEntity): NetworkResult<RegistroEntrenamientoResponseEntity> {
        return try {
            val response = apiService.createRegistro(registro)
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error("${Constantes.ERROR_GUARDAR_REGISTRO}${response.code()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error("${Constantes.ERROR_DE_RED}${e.message}")
        }
    }

    suspend fun getRegistros(): NetworkResult<List<RegistroEntrenamientoResponseEntity>> {
        return try {
            val response = apiService.getRegistros()
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error(Constantes.ERROR_OBTENER_REGISTROS)
            }
        } catch (e: Exception) {
            NetworkResult.Error("${Constantes.ERROR_DE_RED}${e.message}")
        }
    }
}