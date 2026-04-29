package com.example.powertrack_app.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen : NavKey{
    @Serializable
    data object Login : Screen
    @Serializable
    data object Register : Screen
    @Serializable
    data object Home : Screen

    @Serializable
    data class DetalleEntrenamiento(val id: Long) : Screen
    @Serializable
    data object ParaTi : Screen

    @Serializable
    data object Calendario : Screen
     @Serializable
    data object NuevoRegistro : Screen
    @Serializable
    data object PerfilUsuario : Screen
    @Serializable
    data object PerfilForm : Screen
}