package com.example.powertrack_app.ui.screens.gym.listadoEntrenamiento

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powertrack_app.common.Constantes
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.domain.usecase.gym.DeleteEntrenamientoUseCase
import com.example.powertrack_app.domain.usecase.gym.GetEntrenamientosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ListaViewModel @Inject constructor(
    private val getEntrenamientosUseCase: GetEntrenamientosUseCase,
    private val deleteEntrenamientoUseCase: DeleteEntrenamientoUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ListaState())
    val state = _state.asStateFlow()

    init { cargar() }

    fun cargar() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val res = getEntrenamientosUseCase()) {
                is NetworkResult.Success -> {
                    _state.update { it.copy(entrenamientos = res.data, isLoading = false, error = null) }
                }
                is NetworkResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = res.message) }
                }
            }
        }
    }

    fun eliminar(id: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (deleteEntrenamientoUseCase(id)) {
                is NetworkResult.Success -> {
                    cargar()
                }
                is NetworkResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = Constantes.ERROR_ELIMINAR_ENTRENAMIENTO) }
                }
            }
        }
    }
}

