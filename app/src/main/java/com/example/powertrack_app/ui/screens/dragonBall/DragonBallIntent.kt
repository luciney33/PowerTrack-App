package com.example.powertrack_app.ui.screens.dragonBall

sealed interface DragonBallIntent {
    data object LoadCharacters : DragonBallIntent
}
