package com.example.powertrack_app.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powertrack_app.data.repository.GymRepository
import com.example.powertrack_app.data.security.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogoutViewModel @Inject constructor(
    private val gymRepository: GymRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    fun logout(onLogoutFinished: () -> Unit) {
        viewModelScope.launch {
            gymRepository.logout()
            sessionManager.clearSession()
            onLogoutFinished()
        }
    }
}
