package com.example.powertrack_app.ui.screens.registroEntrenamiento

import com.example.powertrack_app.domain.model.Ejercicio

data class RegistroState(
    val ejercicios: List<Ejercicio> = emptyList(),
    val detalles: List<DetalleForm> = emptyList(),
    val observaciones: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

data class DetalleForm(
    val ejercicioId: Long,
    val nombreEjercicio: String,
    val tipoEjercicio: String,
    // Fuerza
    val series: String = "3",
    val repeticiones: String = "10",
    val peso: String = "0.0",
    // Cardio
    val duracionMinutos: String = "30",
    val velocidad: String = "8.0",
    val inclinacion: String = "0.0",
    val kcalGastadas: String = "0"
) {
    val esCardio: Boolean get() = tipoEjercicio == "CARDIO"
}