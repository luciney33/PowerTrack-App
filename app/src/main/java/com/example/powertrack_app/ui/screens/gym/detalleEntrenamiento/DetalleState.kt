package com.example.powertrack_app.ui.screens.gym.detalleEntrenamiento

import com.example.powertrack_app.domain.model.Ejercicio

data class DetalleState(
    val id: Long = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val isLoading: Boolean = false,
    val ejercicios: List<Ejercicio> = emptyList(),
    val error: String? = null,
    val saveSuccess: Boolean = false
)