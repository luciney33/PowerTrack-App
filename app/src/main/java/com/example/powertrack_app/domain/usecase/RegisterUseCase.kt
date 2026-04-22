package com.example.powertrack_app.domain.usecase

import android.util.Base64
import com.example.powertrack_app.common.Constantes
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.GymRepository
import com.example.powertrack_app.data.remote.entity.UsuarioEntity
import com.example.powertrack_app.data.security.CryptoManager
import com.example.powertrack_app.data.security.SessionManager
import com.example.powertrack_app.domain.model.Usuario
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val gymRepository: GymRepository,
    private val cryptoManager: CryptoManager,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(
        username: String,
        email: String,
        nombre: String,
        password: String
    ): NetworkResult<Usuario> {
        return try {
            if (password.length < 8) {
                return NetworkResult.Error(Constantes.ERROR_PASSWORD_MINIMA)
            }

            val keyPair = cryptoManager.generateRSAKeyPair()
            val publicKey = keyPair.public
            val privateKey = keyPair.private

            val salt = cryptoManager.generateSalt()
            val derivedKey = cryptoManager.deriveKeyFromPassword(password, salt)

            val iv = cryptoManager.generateIV()
            val privateKeyBytes = privateKey.encoded
            val encryptedPrivateKey = cryptoManager.encryptAES_GCM(privateKeyBytes, derivedKey, iv)

            val publicKeyBytes = publicKey.encoded
            cryptoManager.saveEncryptedKeys(
                username = username,
                encryptedPrivateKey = encryptedPrivateKey,
                salt = salt,
                ivPrivateKey = iv,
                publicKey = publicKeyBytes
            )

            val publicKeyBase64 = Base64.encodeToString(publicKeyBytes, Base64.NO_WRAP)

            val usuarioEntity = UsuarioEntity(
                id = 0,
                username = username,
                email = email,
                nombre = nombre,
                password = password,
                publicKey = publicKeyBase64,
                certificado = null
            )

            val result = gymRepository.register(usuarioEntity)

            when (result) {
                is NetworkResult.Success -> {
                    val usuario = result.data
                    usuario.certificado?.let { certificadoBytes ->
                        val certificadoBase64 = Base64.encodeToString(certificadoBytes, Base64.NO_WRAP)
                        cryptoManager.saveCertificado(certificadoBase64)
                    }
                    sessionManager.savePasswordInSession(password)
                    NetworkResult.Success(usuario)
                }
                is NetworkResult.Error -> {
                    cryptoManager.clearAllKeys(username)
                    result
                }
            }
        } catch (e: Exception) {
            try {
                cryptoManager.clearAllKeys(username)
            } catch (_: Exception) {
            }
            NetworkResult.Error("${Constantes.ERROR_GENERAR_CLAVES}${e.message}")
        }
    }
}
