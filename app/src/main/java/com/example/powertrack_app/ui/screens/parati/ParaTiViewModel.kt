package com.example.powertrack_app.ui.screens.parati

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.AuthRepository
import com.example.powertrack_app.domain.usecase.gym.GetPlanRecomendadoUseCase
import com.example.powertrack_app.domain.usecase.gym.GetRutinaRecomendadaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ParaTiViewModel @Inject constructor(
    private val getRutinaRecomendada: GetRutinaRecomendadaUseCase,
    private val getPlanRecomendado: GetPlanRecomendadoUseCase,
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ParaTiState())
    val state: StateFlow<ParaTiState> = _state.asStateFlow()

    init {
        cargarDatos()
    }

    fun cargarDatos() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val rutinaDeferred = async { getRutinaRecomendada() }
            val planDeferred = async { getPlanRecomendado() }
            val perfilDeferred = async { repository.getPerfil() }

            val rutinaResult = rutinaDeferred.await()
            val planResult = planDeferred.await()
            val perfilResult = perfilDeferred.await()

            val usuario = if (perfilResult is NetworkResult.Success) perfilResult.data else null

            _state.update {
                it.copy(
                    isLoading = false,
                    rutina = if (rutinaResult is NetworkResult.Success) rutinaResult.data else null,
                    plan = if (planResult is NetworkResult.Success) planResult.data else null,
                    descripcionRutina = usuario?.descripcionRutina,
                    consejosNutricion = usuario?.consejosNutricion
                )
            }
        }
    }
}