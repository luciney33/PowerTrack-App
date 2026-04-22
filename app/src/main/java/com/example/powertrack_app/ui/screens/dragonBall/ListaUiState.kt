package com.example.powertrack_app.ui.screens.dragonBall

import com.example.powertrack_app.domain.model.DragonBallCharacter

data class ListaUiState(
    val characters: List<DragonBallCharacter> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

