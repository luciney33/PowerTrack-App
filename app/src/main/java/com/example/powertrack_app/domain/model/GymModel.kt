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
data class Rutina(
    val id: Long,
    val nombre: String,
    val descripcion: String,
    val tipo: Int,
    val ejercicios: List<Ejercicio>
)

data class PlanNutricional(
    val id: Long,
    val nombre: String,
    val descripcion: String,
    val tipo: Int,
    val caloriasObjetivo: Int,
    val proteinasObjetivo: Double,
    val carbohidratosObjetivo: Double,
    val grasasObjetivo: Double,
    val comidas: List<Comida>
)

data class Comida(
    val id: Long,
    val nombre: String,
    val calorias: Int,
    val proteinas: Double,
    val carbohidratos: Double,
    val grasas: Double,
    val categoria: String
)
data class RegistroEntrenamiento(
    val id: Long,
    val rutinaId: Long,
    val fecha: String,
    val observaciones: String,
    val detalles: List<RegistroDetalle>
)

data class RegistroDetalle(
    val ejercicioId: Long,
    val nombreEjercicio: String,
    val series: Int,
    val repeticiones: Int,
    val peso: Double
)