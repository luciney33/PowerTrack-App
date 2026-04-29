package com.example.powertrack_app.ui.screens.ejercicios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.domain.usecase.gym.GetEjerciciosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EjerciciosViewModel @Inject constructor(
    private val getEjerciciosUseCase: GetEjerciciosUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EjerciciosState())
    val state: StateFlow<EjerciciosState> = _state.asStateFlow()

    init {
        cargarEjercicios()
    }

    fun cargarEjercicios() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = getEjerciciosUseCase()) {
                is NetworkResult.Success -> _state.update {
                    it.copy(isLoading = false, ejercicios = result.data)
                }
                is NetworkResult.Error -> _state.update {
                    it.copy(isLoading = false, error = result.message)
                }
            }
        }
    }
}