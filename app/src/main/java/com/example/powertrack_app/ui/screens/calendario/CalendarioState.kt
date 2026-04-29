package com.example.powertrack_app.ui.screens.calendario

import com.example.powertrack_app.data.remote.entity.RegistroEntrenamientoResponseEntity
import java.time.LocalDate

data class CalendarioState(
    val registros: List<RegistroEntrenamientoResponseEntity> = emptyList(),
    val diasEntrenados: Set<LocalDate> = emptySet(),
    val registroSeleccionado: RegistroEntrenamientoResponseEntity? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)