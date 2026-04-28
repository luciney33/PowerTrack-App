package com.example.powertrack_app.ui.screens.perfil

sealed class PerfilFormEvent {
    data class GeneroChanged(val value: Int) : PerfilFormEvent()
    data class EdadChanged(val value: Int) : PerfilFormEvent()
    data class ObjetivoChanged(val value: Int) : PerfilFormEvent()
    data class NivelChanged(val value: Int) : PerfilFormEvent()
    data class DiasChanged(val value: Int) : PerfilFormEvent()
    data class LesionChanged(val value: Int) : PerfilFormEvent()
    data class PreferenciaChanged(val value: Int) : PerfilFormEvent()
    object Guardar : PerfilFormEvent()
}