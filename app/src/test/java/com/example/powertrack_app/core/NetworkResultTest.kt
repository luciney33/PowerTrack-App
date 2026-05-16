package com.example.powertrack_app.core

import com.example.powertrack_app.common.NetworkResult
import org.junit.Assert.*
import org.junit.Test

class NetworkResultTest {

    @Test
    fun `Success almacena el dato correctamente`() {
        val result = NetworkResult.Success("dato")
        assertTrue(result is NetworkResult.Success)
        assertEquals("dato", result.data)
    }

    @Test
    fun `Error almacena el mensaje correctamente`() {
        val result = NetworkResult.Error<String>("algo falló")
        assertTrue(result is NetworkResult.Error)
        assertEquals("algo falló", result.message)
    }

    @Test
    fun `Success no es Error`() {
        val result: NetworkResult<String> = NetworkResult.Success("ok")
        assertFalse(result is NetworkResult.Error)
    }

    @Test
    fun `then transforma Success correctamente`() {
        val result = NetworkResult.Success(5)
        val transformed = result.then { NetworkResult.Success(it * 2) }
        assertTrue(transformed is NetworkResult.Success)
        assertEquals(10, (transformed as NetworkResult.Success).data)
    }

    @Test
    fun `then propaga Error sin transformar`() {
        val result = NetworkResult.Error<Int>("fallo")
        val transformed = result.then { NetworkResult.Success(it * 2) }
        assertTrue(transformed is NetworkResult.Error)
        assertEquals("fallo", (transformed as NetworkResult.Error).message)
    }

    @Test
    fun `Success con objeto complejo`() {
        data class Persona(val nombre: String, val edad: Int)
        val persona = Persona("Ana", 25)
        val result = NetworkResult.Success(persona)
        assertEquals("Ana", result.data.nombre)
        assertEquals(25, result.data.edad)
    }

    @Test
    fun `Success con lista vacia`() {
        val result = NetworkResult.Success(emptyList<String>())
        assertTrue(result.data.isEmpty())
    }
}
