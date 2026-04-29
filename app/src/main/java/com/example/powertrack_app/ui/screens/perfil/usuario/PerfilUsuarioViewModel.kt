package com.example.powertrack_app.ui.screens.perfil.usuario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.GymRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerfilUsuarioViewModel @Inject constructor(
    private val repository: GymRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PerfilUsuarioState())
    val state: StateFlow<PerfilUsuarioState> = _state.asStateFlow()

    init {
        cargarPerfil()
    }

    fun cargarPerfil() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = repository.getPerfil()) {
                is NetworkResult.Success -> _state.update {
                    it.copy(isLoading = false, usuario = result.data)
                }
                is NetworkResult.Error -> _state.update {
                    it.copy(isLoading = false, error = result.message)
                }
            }
        }
    }
}