package com.example.powertrack_app.ui.screens.parati

import com.example.powertrack_app.domain.model.PlanNutricional
import com.example.powertrack_app.domain.model.Rutina

data class ParaTiState(
    val rutina: Rutina? = null,
    val plan: PlanNutricional? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val descripcionRutina: String? = null,
    val consejosNutricion: String? = null,
)