package com.example.powertrack_app.ui.screens.detallePlan

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powertrack_app.common.Constantes
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.domain.usecase.gym.GetPlanByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetallePlanViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPlanById: GetPlanByIdUseCase,
) : ViewModel() {

    private val planId: Long = checkNotNull(savedStateHandle["planId"])

    private val _state = MutableStateFlow(DetallePlanState(isLoading = true))
    val state: StateFlow<DetallePlanState> = _state.asStateFlow()

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = getPlanById(planId)) {
                is NetworkResult.Success -> _state.update {
                    it.copy(plan = result.data, isLoading = false)
                }
                is NetworkResult.Error -> _state.update {
                    it.copy(error = result.message, isLoading = false)
                }
            }
        }
    }

    fun verFoto(comidaNombre: String, imagenUrl: String) {
        val dialog = if (imagenUrl.isBlank()) {
            FotoDialogState(comidaNombre, error = "Imagen no disponible para esta comida")
        } else {
            val fullUrl = "${Constantes.URL_BASE_EMULATOR}$imagenUrl"
            FotoDialogState(comidaNombre, fotoUrl = fullUrl)
        }
        _state.update { it.copy(fotoDialog = dialog) }
    }

    fun cerrarDialog() {
        _state.update { it.copy(fotoDialog = null) }
    }
}
