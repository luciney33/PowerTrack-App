package com.example.powertrack_app.ui.screens.secretos.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.domain.model.Secreto
import com.example.powertrack_app.domain.usecase.secretos.ObtenerSecretosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListaSecretosViewModel @Inject constructor(
    private val obtenerSecretosUseCase: ObtenerSecretosUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ListaSecretosState())
    val state = _state.asStateFlow()

    init {
        cargarSecretos()
    }

    fun cargarSecretos() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = obtenerSecretosUseCase()) {
                is NetworkResult.Success<List<Secreto>> -> {
                    _state.update { it.copy(secretos = result.data, isLoading = false) }
                }
                is NetworkResult.Error<List<Secreto>> -> {
                    _state.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }
}

