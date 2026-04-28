package com.example.powertrack_app.ui.common


sealed interface UiEvent {
    object LoginSuccess : UiEvent
    object LoginSuccessNeedsPerfil : UiEvent
    object RegisterSuccess : UiEvent
    data class ShowError(val message: String) : UiEvent
    data class ShowSnackbar(val message: String) : UiEvent
    data class NavigateTo(val route: String) : UiEvent
}