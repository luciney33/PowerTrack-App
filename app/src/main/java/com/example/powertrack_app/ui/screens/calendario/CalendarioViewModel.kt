package com.example.powertrack_app.ui.screens.calendario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.domain.usecase.gym.GetRegistrosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarioViewModel @Inject constructor(
    private val getRegistrosUseCase: GetRegistrosUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CalendarioState())
    val state: StateFlow<CalendarioState> = _state.asStateFlow()

    init {
        cargarRegistros()
    }

    fun cargarRegistros() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = getRegistrosUseCase()) {
                is NetworkResult.Success -> {
                    val registros = result.data
                    val dias = registros.mapNotNull { registro ->
                        try { LocalDate.parse(registro.fecha) } catch (e: Exception) { null }
                    }.toSet()
                    _state.update {
                        it.copy(isLoading = false, registros = registros, diasEntrenados = dias)
                    }
                }
                is NetworkResult.Error -> _state.update {
                    it.copy(isLoading = false, error = result.message)
                }
            }
        }
    }

    fun seleccionarDia(fecha: LocalDate) {
        val registro = _state.value.registros.find { registro ->
            try { LocalDate.parse(registro.fecha) == fecha } catch (e: Exception) { false }
        }
        _state.update { it.copy(registroSeleccionado = registro) }
    }
}