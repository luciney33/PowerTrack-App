package com.example.powertrack_app.common

object Constantes {
    const val ERROR_DEL_SERVIDOR = "Error del servidor: "
    const val ERROR_DE_CONEXION = "Error de conexión: "
    const val ERROR_DE_RED = "Error de red: "
    const val ERROR_FALLO_CONEXION = "Fallo de conexión: "
    const val ERROR_USUARIO_PASSWORD_INCORRECTOS = "Usuario o contraseña incorrectos"
    const val ERROR_NO_CARGAR_ENTRENAMIENTOS = "No se pudieron cargar los entrenamientos"
    const val ERROR_ENTRENAMIENTO_NO_ENCONTRADO = "Entrenamiento no encontrado"
    const val ERROR_BUSCAR_DETALLE = "Error al buscar detalle"
    const val ERROR_GUARDAR_SERVIDOR = "Error al guardar en el servidor"
    const val ERROR_FALLO_RED = "Fallo de red"
    const val ERROR_NO_ELIMINAR = "No se pudo eliminar"
    const val ERROR_CARGAR_EJERCICIOS = "Error al cargar lista de ejercicios"
    const val ERROR_GENERICO = "Error: "

    const val ID = "id"

    const val API_AUTH_LOGIN = "api/auth/login"
    const val API_AUTH_REGISTER = "api/auth/register"
    const val API_AUTH_ACTIVAR = "api/auth/activar"
    const val API_ENTRENAMIENTOS = "api/entrenamientos"
    const val API_ENTRENAMIENTOS_ID = "api/entrenamientos/{id}"
    const val API_EJERCICIOS = "api/ejercicios"
    const val API_PERFIL = "api/perfil"
    const val API_PERFIL_COMPLETAR = "api/perfil/completar"

    const val RETROFIT_GYMAPI = "GymRetrofit"


    const val PREFS_NAME = "prefs_seguras"
    const val PREF_ACCESS_TOKEN = "access_token"
    const val PREF_REFRESH_TOKEN = "refresh_token"

    const val TEXT_APP_NAME = "PowerTrack"
    const val TEXT_LABEL_USUARIO = "Usuario"
    const val TEXT_LABEL_PASSWORD = "Contraseña"
    const val TEXT_BUTTON_LOGIN = "ENTRAR"
    const val TEXT_NO_ACCOUNT = "¿No tienes cuenta? Regístrate aquí"
    const val TEXT_USUARIO_EJEMPLO = "UsuarioEjemplo"

    const val TEXT_TITULO_REGISTRO = "Registro"
    const val TEXT_CREAR_CUENTA = "Crear Cuenta"
    const val TEXT_LABEL_EMAIL = "Email"
    const val TEXT_LABEL_NOMBRE_COMPLETO = "Nombre completo"
    const val TEXT_LABEL_CONFIRMAR_PASSWORD = "Confirmar contraseña"
    const val TEXT_BUTTON_REGISTRARSE = "REGISTRARSE"
    const val TEXT_DESCRIPCION_VOLVER = "Volver"

    const val TEXT_NOMBRE_ENTRENAMIENTO = "Nombre Entrenamiento"
    const val TEXT_DESCRIPCION = "Descripción"
    const val TEXT_BUTTON_CREAR = "CREAR"
    const val TEXT_BUTTON_ACTUALIZAR = "ACTUALIZAR"
    const val TEXT_RUTINA_EJEMPLO = "Rutina A"
    const val TEXT_NOTAS_PROGRESION = "Notas sobre la progresión de cargas para esta semana."
    const val TEXT_EJERCICIOS_RUTINA = "Ejercicios de esta rutina"

    const val TEXT_EMPUJE = "Empuje (Pecho/Tríceps)"
    const val TEXT_TRACCION = "Tracción (Espalda/Bíceps)"
    const val TEXT_PIERNA = "Pierna Completa"
    const val TEXT_ENFOQUE_FUERZA = "Enfoque en fuerza"
    const val TEXT_HIPERTROFIA = "Hipertrofia"
    const val TEXT_DIA_PESADO = "Día pesado"

    const val URL_BASE_EMULATOR = "http://10.0.2.2:8080"

    const val TEXT_YA_TIENE_CUENTA = "¿Ya tienes cuenta? Inicia sesión"
    const val TEXT_OCULTAR_PASSWORD = "Ocultar contraseña"
    const val TEXT_MOSTRAR_PASSWORD = "Mostrar contraseña"
    const val USER = "USER"

    const val AUTH_HEADER = "Authorization"
    const val AUTH_BEARER_PREFIX = "Bearer "

    const val ERROR_EMAIL_INVALIDO = "El email no es válido. Debe contener '@' y un dominio (ej: usuario@ejemplo.com)"
    const val ERROR_PASSWORDS_NO_COINCIDEN = "Las contraseñas no coinciden"
    const val ERROR_PASSWORD_VACIA = "La contraseña no puede estar vacía"
    const val ERROR_DESCONOCIDO = "Error desconocido"
    const val REGISTRO_EXITOSO_MENSAJE = "Se ha registrado correctamente"

    const val ERROR_ELIMINAR_ENTRENAMIENTO = "Error al eliminar el entrenamiento"
    const val ERROR_CARGAR_ENTRENAMIENTO = "Error al cargar el entrenamiento"
    const val ERROR_GUARDAR_ENTRENAMIENTO = "Error al guardar el entrenamiento"
    const val ERROR_NOMBRE_VACIO = "El nombre no puede estar vacío"
    const val ERROR_PASSWORD_MINIMA = "La contraseña debe tener al menos 8 caracteres"

}
