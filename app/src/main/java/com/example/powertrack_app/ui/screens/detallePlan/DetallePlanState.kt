package com.example.powertrack_app.ui.screens.detallePlan

import com.example.powertrack_app.domain.model.PlanNutricional

data class DetallePlanState(
    val plan: PlanNutricional? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val fotoDialog: FotoDialogState? = null
)

data class FotoDialogState(
    val comidaNombre: String,
    val fotoUrl: String? = null,
    val error: String? = null
)
