package com.example.powertrack_app.data.repository

import com.example.powertrack_app.common.Constantes
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.remote.api.DragonBallApiService
import com.example.powertrack_app.data.remote.entity.toDomain
import com.example.powertrack_app.domain.model.DragonBallCharacter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DragonBallRepository @Inject constructor(
    private val apiService: DragonBallApiService,
) {

    suspend fun getCharacters(page: Int = 1): NetworkResult<List<DragonBallCharacter>> {
        return try {
            val response = apiService.getCharacters(page)

            if (response.isSuccessful) {
                val dragonBallResponse = response.body()
                val domainList =
                    dragonBallResponse?.characterEntities?.map { it.toDomain() } ?: emptyList()
                NetworkResult.Success(domainList)

            } else {
                NetworkResult.Error("${Constantes.ERROR_DEL_SERVIDOR}${response.code()} ${response.message()}")
            }

        } catch (e: Exception) {
            NetworkResult.Error("${Constantes.ERROR_DE_CONEXION}${e.message}")
        }
    }
}