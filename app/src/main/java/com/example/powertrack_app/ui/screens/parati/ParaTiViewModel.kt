package com.example.powertrack_app.ui.screens.parati

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.domain.usecase.gym.GetPlanRecomendadoUseCase
import com.example.powertrack_app.domain.usecase.gym.GetRutinaRecomendadaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParaTiViewModel @Inject constructor(
    private val getRutinaRecomendada: GetRutinaRecomendadaUseCase,
    private val getPlanRecomendado: GetPlanRecomendadoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ParaTiState())
    val state: StateFlow<ParaTiState> = _state.asStateFlow()

    init {
        cargarDatos()
    }

    fun cargarDatos() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val rutinaResult = getRutinaRecomendada()
            val planResult = getPlanRecomendado()

            val rutina = if (rutinaResult is NetworkResult.Success) rutinaResult.data else null
            val plan = if (planResult is NetworkResult.Success) planResult.data else null
            val error = when {
                rutinaResult is NetworkResult.Error -> rutinaResult.message
                planResult is NetworkResult.Error -> planResult.message
                else -> null
            }

            _state.update {
                it.copy(
                    isLoading = false,
                    rutina = rutina,
                    plan = plan,
                    error = error
                )
            }
        }
    }
}