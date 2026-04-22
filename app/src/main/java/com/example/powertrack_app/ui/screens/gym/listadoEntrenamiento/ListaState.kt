package com.example.powertrack_app.ui.screens.gym.listadoEntrenamiento

import com.example.powertrack_app.domain.model.Entrenamiento

data class ListaState(
    val entrenamientos: List<Entrenamiento> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

