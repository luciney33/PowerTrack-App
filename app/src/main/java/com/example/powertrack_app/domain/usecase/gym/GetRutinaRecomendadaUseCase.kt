package com.example.powertrack_app.domain.usecase.gym

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.TrainingRepository
import com.example.powertrack_app.domain.model.Rutina
import javax.inject.Inject

class GetRutinaRecomendadaUseCase @Inject constructor(
    private val repository: TrainingRepository
) {
    suspend operator fun invoke(): NetworkResult<Rutina> {
        return repository.getRutinaRecomendada()
    }
}