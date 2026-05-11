package com.example.powertrack_app.ui.screens.detalleRutina

import com.example.powertrack_app.domain.model.Rutina

data class DetalleRutinaState(
    val rutina: Rutina? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val gifDialog: GifDialogState? = null
)

data class GifDialogState(
    val ejercicioNombre: String,
    val gifUrl: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)