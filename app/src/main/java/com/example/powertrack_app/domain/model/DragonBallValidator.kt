package com.example.powertrack_app.domain.model

import com.example.powertrack_app.common.Constantes
import com.example.powertrack_app.common.NetworkResult

object DragonBallValidator {
    fun isPageNotNegative(page: Int): NetworkResult<Boolean> {
        return if (page < 0) NetworkResult.Error(Constantes.ERROR_PAGE_NUMBER) else NetworkResult.Success(
            true
        )
    }

    fun isPageLimit(page: Int): NetworkResult<Boolean> {
        return if (page > 12) NetworkResult.Error(Constantes.ERROR_PAGE_NUMBER) else NetworkResult.Success(
            true
        )
    }
}
