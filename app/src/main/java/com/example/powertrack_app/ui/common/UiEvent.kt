package com.example.powertrack_app.ui.common


sealed interface UiEvent {
    data class ShowError(val message: String) : UiEvent
    data class ShowSnackbar(val message: String) : UiEvent
    data object LoginSuccess : UiEvent
    data object RegisterSuccess : UiEvent
}