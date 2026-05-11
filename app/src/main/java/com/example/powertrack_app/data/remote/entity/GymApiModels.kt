package com.example.powertrack_app.data.remote.entity

import com.example.powertrack_app.common.Constantes
import com.example.powertrack_app.domain.model.Comida
import com.example.powertrack_app.domain.model.Ejercicio
import com.example.powertrack_app.domain.model.Entrenamiento
import com.example.powertrack_app.domain.model.PlanNutricional
import com.example.powertrack_app.domain.model.Rutina
import com.example.powertrack_app.domain.model.Usuario

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val usuario: UsuarioEntity,
    val message: String,
    val requires2FA: Boolean
)
data class LoginRequest(val username: String, val password: String)
data class EjercicioEntity(
    val id: Long,
    val nombre: String,
    val tipoEntrenamiento: String,
    val imagenUrl: String,
    val descripcion: String,
    val series: Int? = null,
    val repeticiones: Int? = null,
    val descansoSeg: Int? = null
)

data class EntrenamientoEntity(
    val id: Long,
    val usuarioId: Long,
    val nombre: String,
    val descripcion: String,
    val ejercicios: List<EjercicioEntity>? = emptyList()
)


data class UsuarioEntity(
    val id: Long,
    val username: String,
    val email: String,
    val nombre: String,
    val password: String,
    val rol: String = Constantes.USER,
    val activo: Boolean = true,
    val formularioCompletado: Boolean = false,
    val recomendacion: Int? = null,
    val publicKey: String? = null,
    val certificado: String? = null,
    val descripcionRutina: String? = null,
    val consejosNutricion: String? = null
)

data class PerfilRequestEntity(
    val genero: Int,
    val edad: Int,
    val objetivo: Int,
    val nivel: Int,
    val diasEntrenamiento: Int,
    val lesion: Int,
    val preferencia: Int,
    val pesoCat: Int = 1
)

data class RutinaEntity(
    val id: Long,
    val nombre: String,
    val descripcion: String,
    val tipo: Int,
    val ejercicios: List<EjercicioEntity>? = emptyList()
)

data class PlanNutricionalEntity(
    val id: Long,
    val nombre: String,
    val descripcion: String,
    val tipo: Int,
    val caloriasObjetivo: Int,
    val proteinasObjetivo: Double,
    val carbohidratosObjetivo: Double,
    val grasasObjetivo: Double,
    val comidas: List<ComidaEntity>? = emptyList()
)

data class ComidaEntity(
    val id: Long,
    val nombre: String,
    val calorias: Int,
    val proteinas: Double,
    val carbohidratos: Double,
    val grasas: Double,
    val categoria: String
)

data class RegistroEntrenamientoRequestEntity(
    val rutinaId: Long,
    val fecha: String,
    val observaciones: String,
    val detalles: List<RegistroDetalleRequestEntity>
)

data class RegistroDetalleRequestEntity(
    val ejercicioId: Long? = null,
    val series: Int? = null,
    val repeticiones: Int? = null,
    val peso: Double? = null,
    val duracionMinutos: Int? = null,
    val velocidad: Double? = null,
    val inclinacion: Double? = null,
    val kcalGastadas: Int? = null
)

data class RegistroEntrenamientoResponseEntity(
    val id: Long,
    val rutinaId: Long,
    val fecha: String,
    val observaciones: String,
    val detalles: List<RegistroDetalleResponseEntity>
)

data class RegistroDetalleResponseEntity(
    val id: Long,
    val ejercicio: EjercicioEntity,
    val series: Int,
    val repeticiones: Int,
    val peso: Double,
    val duracionMinutos: Int? = null,
    val velocidad: Double? = null,
    val inclinacion: Double? = null,
    val kcalGastadas: Int? = null
)
fun RutinaEntity.toDomain() = Rutina(
    id = id,
    nombre = nombre,
    descripcion = descripcion,
    tipo = tipo,
    ejercicios = ejercicios?.map { it.toDomain() } ?: emptyList()
)

fun PlanNutricionalEntity.toDomain() = PlanNutricional(
    id = id,
    nombre = nombre,
    descripcion = descripcion,
    tipo = tipo,
    caloriasObjetivo = caloriasObjetivo,
    proteinasObjetivo = proteinasObjetivo,
    carbohidratosObjetivo = carbohidratosObjetivo,
    grasasObjetivo = grasasObjetivo,
    comidas = comidas?.map { it.toDomain() } ?: emptyList()
)

fun ComidaEntity.toDomain() = Comida(
    id = id,
    nombre = nombre,
    calorias = calorias,
    proteinas = proteinas,
    carbohidratos = carbohidratos,
    grasas = grasas,
    categoria = categoria
)

fun EjercicioEntity.toDomain() = Ejercicio(
    id = id,
    nombre = nombre,
    tipo = tipoEntrenamiento,
    imageUrl = imagenUrl,
    descripcion = descripcion,
    series = series,
    repeticiones = repeticiones,
    descansoSeg = descansoSeg
)

fun Ejercicio.toEntity() = EjercicioEntity(
    id = id,
    nombre = nombre,
    tipoEntrenamiento = tipo,
    imagenUrl = imageUrl,
    descripcion = descripcion,
    series = series,
    repeticiones = repeticiones,
    descansoSeg = descansoSeg
)

fun EntrenamientoEntity.toDomain() = Entrenamiento(
    id = id,
    nombre = nombre,
    descripcion = descripcion,
    ejercicios = ejercicios?.map { it.toDomain() } ?: emptyList()
)

fun Entrenamiento.toEntity(usuarioId: Long) = EntrenamientoEntity(
    id = id,
    usuarioId = usuarioId,
    nombre = nombre,
    descripcion = descripcion,
    ejercicios = ejercicios.map { it.toEntity() }
)

fun UsuarioEntity.toDomain() = Usuario(
    id = id,
    username = username,
    email = email,
    nombre = nombre,
    rol = rol,
    formularioCompletado = formularioCompletado,
    recomendacion = recomendacion,
    publicKey = publicKey?.let { android.util.Base64.decode(it, android.util.Base64.DEFAULT) },
    certificado = certificado?.let { android.util.Base64.decode(it, android.util.Base64.DEFAULT) },
    certificadoVerificado = false,
    descripcionRutina = descripcionRutina,
    consejosNutricion = consejosNutricion
)