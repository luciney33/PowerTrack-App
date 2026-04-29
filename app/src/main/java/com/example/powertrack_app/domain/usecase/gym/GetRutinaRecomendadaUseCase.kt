package com.example.powertrack_app.domain.usecase.gym

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.GymRepository
import com.example.powertrack_app.domain.model.Rutina
import javax.inject.Inject

class GetRutinaRecomendadaUseCase @Inject constructor(
    private val repository: GymRepository
) {
    suspend operator fun invoke(): NetworkResult<Rutina> {
        return repository.getRutinaRecomendada()
    }
}