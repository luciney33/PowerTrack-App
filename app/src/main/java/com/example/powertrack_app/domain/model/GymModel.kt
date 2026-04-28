package com.example.powertrack_app.domain.model

data class Ejercicio(
    val id: Long,
    val nombre: String,
    val tipo: String,
    val imageUrl: String,
    val descripcion: String
)

data class Entrenamiento(
    val id: Long,
    val nombre: String,
    val descripcion: String,
    val ejercicios: List<Ejercicio>
)

data class Usuario(
    val id: Long,
    val username: String,
    val email: String,
    val nombre: String,
    val rol: String,
    val formularioCompletado: Boolean = false,
    val recomendacion: Int? = null,
    val publicKey: ByteArray? = null,
    val certificado: ByteArray? = null,
    val certificadoVerificado: Boolean = false
)
data class PerfilRequest(
    val genero: Int,
    val edad: Int,
    val objetivo: Int,
    val nivel: Int,
    val diasEntrenamiento: Int,
    val lesion: Int,
    val preferencia: Int
)
