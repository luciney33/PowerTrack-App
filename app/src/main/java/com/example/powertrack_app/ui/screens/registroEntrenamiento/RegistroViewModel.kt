package com.example.powertrack_app.ui.screens.registroEntrenamiento
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.remote.entity.RegistroDetalleRequestEntity
import com.example.powertrack_app.data.remote.entity.RegistroEntrenamientoRequestEntity
import com.example.powertrack_app.domain.usecase.gym.CreateRegistroUseCase
import com.example.powertrack_app.domain.usecase.gym.GetEjerciciosUseCase
import com.example.powertrack_app.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class RegistroViewModel @Inject constructor(
    private val getEjerciciosUseCase: GetEjerciciosUseCase,
    private val createRegistroUseCase: CreateRegistroUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegistroState())
    val state: StateFlow<RegistroState> = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _fecha = MutableStateFlow(LocalDate.now().toString())

    init {
        cargarEjercicios()
    }

    fun setFecha(fecha: String) {
        if (fecha.isNotBlank()) _fecha.value = fecha
    }

    private fun cargarEjercicios() {
        viewModelScope.launch {
            when (val result = getEjerciciosUseCase()) {
                is NetworkResult.Success -> _state.update { it.copy(ejercicios = result.data) }
                is NetworkResult.Error -> _state.update { it.copy(error = result.message) }
            }
        }
    }

    fun onEvent(event: RegistroEvent) {
        when (event) {
            is RegistroEvent.AyadirEjercicio -> {
                val nuevo = DetalleForm(
                    ejercicioId = event.ejercicioId,
                    nombreEjercicio = event.nombre,
                    tipoEjercicio = event.tipo
                )
                _state.update { it.copy(detalles = it.detalles + nuevo) }
            }
            is RegistroEvent.DuracionChanged -> updateDetalle(event.index) { it.copy(duracionMinutos = event.value) }
            is RegistroEvent.VelocidadChanged -> updateDetalle(event.index) { it.copy(velocidad = event.value) }
            is RegistroEvent.InclinacionChanged -> updateDetalle(event.index) { it.copy(inclinacion = event.value) }
            is RegistroEvent.KcalChanged -> updateDetalle(event.index) { it.copy(kcalGastadas = event.value) }
            is RegistroEvent.EliminarEjercicio -> {
                _state.update {
                    it.copy(detalles = it.detalles.toMutableList().also { list ->
                        list.removeAt(event.index)
                    })
                }
            }
            is RegistroEvent.SeriesChanged -> updateDetalle(event.index) {
                it.copy(series = event.value)
            }
            is RegistroEvent.RepeticionesChanged -> updateDetalle(event.index) {
                it.copy(repeticiones = event.value)
            }
            is RegistroEvent.PesoChanged -> updateDetalle(event.index) {
                it.copy(peso = event.value)
            }
            is RegistroEvent.ObservacionesChanged -> _state.update {
                it.copy(observaciones = event.value)
            }
            is RegistroEvent.Guardar -> guardar()
        }
    }

    private fun updateDetalle(index: Int, transform: (DetalleForm) -> DetalleForm) {
        _state.update { state ->
            val updated = state.detalles.toMutableList()
            updated[index] = transform(updated[index])
            state.copy(detalles = updated)
        }
    }

    private fun guardar() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val s = _state.value
            val request = RegistroEntrenamientoRequestEntity(
                rutinaId = null,
                fecha = _fecha.value,
                observaciones = s.observaciones,
                detalles = s.detalles.map { d ->
                    if (d.esCardio) {
                        RegistroDetalleRequestEntity(
                            ejercicioId = d.ejercicioId,
                            duracionMinutos = d.duracionMinutos.toIntOrNull() ?: 30,
                            velocidad = d.velocidad.toDoubleOrNull() ?: 8.0,
                            inclinacion = d.inclinacion.toDoubleOrNull() ?: 0.0,
                            kcalGastadas = d.kcalGastadas.toIntOrNull() ?: 0
                        )
                    } else {
                        RegistroDetalleRequestEntity(
                            ejercicioId = d.ejercicioId,
                            series = d.series.toIntOrNull() ?: 3,
                            repeticiones = d.repeticiones.toIntOrNull() ?: 10,
                            peso = d.peso.toDoubleOrNull() ?: 0.0
                        )
                    }
                }
            )
            when (val result = createRegistroUseCase(request)) {
                is NetworkResult.Success -> {
                    _state.update { it.copy(isLoading = false, isSuccess = true, detalles = emptyList(), observaciones = "") }
                    _uiEvent.send(UiEvent.ShowSnackbar("Entrenamiento guardado correctamente"))
                }
                is NetworkResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message) }
                    _uiEvent.send(UiEvent.ShowError(result.message))
                }
            }
        }
    }
}
