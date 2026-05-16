package com.example.powertrack_app.data.security

import com.example.powertrack_app.data.remote.entity.UsuarioEntity
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {

    private val _currentUser = MutableStateFlow<UsuarioEntity?>(null)

    fun setCurrentUser(user: UsuarioEntity?) {
        _currentUser.value = user
    }


    fun clearSession() {
        _currentUser.value = null
    }
}
