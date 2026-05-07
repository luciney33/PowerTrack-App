package com.example.powertrack_app.ui.screens.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.domain.model.PerfilRequest
import com.example.powertrack_app.domain.usecase.gym.CompletarPerfilUseCase
import com.example.powertrack_app.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerfilFormViewModel @Inject constructor(
    private val completarPerfilUseCase: CompletarPerfilUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PerfilFormState())
    val state: StateFlow<PerfilFormState> = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: PerfilFormEvent) {
        when (event) {
            is PerfilFormEvent.GeneroChanged -> _state.update { it.copy(genero = event.value) }
            is PerfilFormEvent.EdadChanged -> _state.update { it.copy(edad = event.value) }
            is PerfilFormEvent.ObjetivoChanged -> _state.update { it.copy(objetivo = event.value) }
            is PerfilFormEvent.NivelChanged -> _state.update { it.copy(nivel = event.value) }
            is PerfilFormEvent.DiasChanged -> _state.update { it.copy(diasEntrenamiento = event.value) }
            is PerfilFormEvent.LesionChanged -> _state.update { it.copy(lesion = event.value) }
            is PerfilFormEvent.PreferenciaChanged -> _state.update { it.copy(preferencia = event.value) }
            is PerfilFormEvent.PesoChanged -> _state.update { it.copy(peso = event.value) }
            is PerfilFormEvent.Guardar -> guardar()
        }
    }

    private fun guardar() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val s = _state.value
            val pesoCat = when (s.peso.toDoubleOrNull() ?: 0.0) {
                in 0.0..59.9   -> 0
                in 60.0..75.0  -> 1
                in 75.1..90.0  -> 2
                in 90.1..110.0 -> 3
                else           -> 4
            }

            val result = completarPerfilUseCase(
                PerfilRequest(
                    genero = s.genero,
                    edad = s.edad,
                    objetivo = s.objetivo,
                    nivel = s.nivel,
                    diasEntrenamiento = s.diasEntrenamiento,
                    lesion = s.lesion,
                    preferencia = s.preferencia,
                    pesoCat = pesoCat
                )
            )
            when (result) {
                is NetworkResult.Success -> {
                    _state.update { it.copy(isLoading = false, isSuccess = true) }
                    _uiEvent.send(UiEvent.NavigateTo("home"))
                }
                is NetworkResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message) }
                    _uiEvent.send(UiEvent.ShowError(result.message))
                }
            }
        }
    }
}