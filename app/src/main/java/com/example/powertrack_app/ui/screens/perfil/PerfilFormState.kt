package com.example.powertrack_app.ui.screens.perfil

data class PerfilFormState(
    val genero: Int = 0,
    val edad: Int = 0,
    val objetivo: Int = 0,
    val nivel: Int = 0,
    val diasEntrenamiento: Int = 3,
    val lesion: Int = 0,
    val preferencia: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)