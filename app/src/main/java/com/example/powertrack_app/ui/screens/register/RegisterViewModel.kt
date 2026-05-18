package com.example.powertrack_app.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powertrack_app.common.Constantes
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.domain.usecase.RegisterUseCase
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
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun resetState() {
        _state.value = RegisterState()
    }

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.UserChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        username = event.username ?: currentState.username,
                        email = event.email ?: currentState.email,
                        nombre = event.nombre ?: currentState.nombre,
                        password = event.password ?: currentState.password,
                        confirmPassword = event.confirmPassword ?: currentState.confirmPassword,
                        error = null
                    )
                }
            }
            RegisterEvent.Register -> {
                val currentState = _state.value
                if (!emailRegex.matches(currentState.email)) {
                    _state.update { it.copy(error = Constantes.ERROR_EMAIL_INVALIDO) }
                    return
                }
                if (currentState.password != currentState.confirmPassword) {
                    _state.update { it.copy(error = Constantes.ERROR_PASSWORDS_NO_COINCIDEN) }
                    return
                }
                if (currentState.password.isBlank()) {
                    _state.update { it.copy(error = Constantes.ERROR_PASSWORD_VACIA) }
                    return
                }
                if (currentState.password.length < 8) {
                    _state.update { it.copy(error = Constantes.ERROR_PASSWORD_MINIMA) }
                    return
                }
                register()
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val currentState = _state.value
            when (val result = registerUseCase(
                username = currentState.username,
                email = currentState.email,
                nombre = currentState.nombre,
                password = currentState.password
            )) {
                is NetworkResult.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _uiEvent.send(UiEvent.RegisterSuccess)
                }
                is NetworkResult.Error -> {
                    _state.update { it.copy(
                        isLoading = false,
                        error = result.message ?: Constantes.ERROR_DESCONOCIDO
                    ) }
                }
            }
        }
    }
}
