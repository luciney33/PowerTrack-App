package com.example.powertrack_app.ui.screens.perfil.usuario

import com.example.powertrack_app.domain.model.Usuario

data class PerfilUsuarioState(
    val usuario: Usuario? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)