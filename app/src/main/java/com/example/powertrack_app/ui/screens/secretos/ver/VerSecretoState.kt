package com.example.powertrack_app.ui.screens.secretos.ver

import com.example.powertrack_app.domain.model.SecretoDescifrado

data class VerSecretoState(
    val secretoDescifrado: SecretoDescifrado? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val deleteSuccess: Boolean = false,
    val autorId: Long? = null
)
