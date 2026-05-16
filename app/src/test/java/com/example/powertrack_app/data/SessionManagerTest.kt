package com.example.powertrack_app.data

import com.example.powertrack_app.common.Constantes
import com.example.powertrack_app.data.remote.entity.UsuarioEntity
import com.example.powertrack_app.data.security.SessionManager
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SessionManagerTest {

    private lateinit var sessionManager: SessionManager

    private val usuarioFake = UsuarioEntity(
        id = 1L,
        username = "luciap",
        email = "lucia@test.com",
        nombre = "Lucia",
        password = "secreta123",
        rol = Constantes.USER,
        formularioCompletado = true
    )

    @Before
    fun setUp() {
        sessionManager = SessionManager()
    }

    @Test
    fun `setCurrentUser no lanza excepcion`() {
        sessionManager.setCurrentUser(usuarioFake)
    }

    @Test
    fun `clearSession no lanza excepcion`() {
        sessionManager.clearSession()
    }

    @Test
    fun `setCurrentUser con null no lanza excepcion`() {
        sessionManager.setCurrentUser(null)
    }

    @Test
    fun `clearSession doble no lanza excepcion`() {
        sessionManager.setCurrentUser(usuarioFake)
        sessionManager.clearSession()
        sessionManager.clearSession()
    }

    @Test
    fun `set y clear ciclo completo sin errores`() {
        sessionManager.setCurrentUser(usuarioFake)
        sessionManager.clearSession()
        sessionManager.setCurrentUser(usuarioFake)
        sessionManager.clearSession()
    }
}
