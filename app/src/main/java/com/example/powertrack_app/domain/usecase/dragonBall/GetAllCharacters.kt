package com.example.powertrack_app.domain.usecase.dragonBall

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.DragonBallRepository
import com.example.powertrack_app.domain.model.DragonBallCharacter
import com.example.powertrack_app.domain.model.DragonBallValidator
import jakarta.inject.Inject

class GetAllCharacters @Inject constructor(private val repository: DragonBallRepository) {

    suspend operator fun invoke(page: Int): NetworkResult<List<DragonBallCharacter>> =
        DragonBallValidator.isPageNotNegative(page)
            .then { DragonBallValidator.isPageLimit(page) }
            .then {
                repository.getCharacters(page)
            }


}
