package com.example.powertrack_app.ui.screens.secretos.lista

import com.example.powertrack_app.domain.model.Secreto

data class ListaSecretosState(
    val secretos: List<Secreto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

