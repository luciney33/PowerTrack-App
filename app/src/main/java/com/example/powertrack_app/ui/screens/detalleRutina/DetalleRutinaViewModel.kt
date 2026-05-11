package com.example.powertrack_app.ui.screens.detalleRutina

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.domain.usecase.gym.GetEjercicioGifUseCase
import com.example.powertrack_app.domain.usecase.gym.GetRutinaByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetalleRutinaViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRutinaById: GetRutinaByIdUseCase,
    private val getEjercicioGif: GetEjercicioGifUseCase
) : ViewModel() {

    private val rutinaId: Long = checkNotNull(savedStateHandle["rutinaId"])

    private val _state = MutableStateFlow(DetalleRutinaState(isLoading = true))
    val state: StateFlow<DetalleRutinaState> = _state.asStateFlow()

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = getRutinaById(rutinaId)) {
                is NetworkResult.Success -> _state.update {
                    it.copy(rutina = result.data, isLoading = false)
                }
                is NetworkResult.Error -> _state.update {
                    it.copy(error = result.message, isLoading = false)
                }
            }
        }
    }

    fun verGif(ejercicioNombre: String) {
        viewModelScope.launch {
            _state.update { it.copy(gifDialog = GifDialogState(ejercicioNombre, isLoading = true)) }
            when (val result = getEjercicioGif(ejercicioNombre)) {
                is NetworkResult.Success -> _state.update {
                    it.copy(gifDialog = it.gifDialog?.copy(gifUrl = result.data, isLoading = false))
                }
                is NetworkResult.Error -> _state.update {
                    it.copy(gifDialog = it.gifDialog?.copy(error = result.message, isLoading = false))
                }
            }
        }
    }

    fun cerrarDialog() {
        _state.update { it.copy(gifDialog = null) }
    }
}