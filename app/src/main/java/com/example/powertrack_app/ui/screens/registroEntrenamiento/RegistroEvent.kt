package com.example.powertrack_app.ui.screens.registroEntrenamiento

sealed class RegistroEvent {
    data class AyadirEjercicio(val ejercicioId: Long, val nombre: String, val tipo: String) : RegistroEvent()
    data class EliminarEjercicio(val index: Int) : RegistroEvent()
    // Fuerza
    data class SeriesChanged(val index: Int, val value: String) : RegistroEvent()
    data class RepeticionesChanged(val index: Int, val value: String) : RegistroEvent()
    data class PesoChanged(val index: Int, val value: String) : RegistroEvent()
    // Cardio
    data class DuracionChanged(val index: Int, val value: String) : RegistroEvent()
    data class VelocidadChanged(val index: Int, val value: String) : RegistroEvent()
    data class InclinacionChanged(val index: Int, val value: String) : RegistroEvent()
    data class KcalChanged(val index: Int, val value: String) : RegistroEvent()
    data class ObservacionesChanged(val value: String) : RegistroEvent()
    object Guardar : RegistroEvent()
}