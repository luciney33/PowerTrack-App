package com.example.powertrack_app.ui.screens.ejercicios

import com.example.powertrack_app.domain.model.Ejercicio

data class EjerciciosState(
    val ejercicios: List<Ejercicio> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)