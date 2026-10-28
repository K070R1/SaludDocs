package com.saluddocs.app.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ValidadorDocumentoTest {

    @Test
    fun nombreVacio_muestraError() {
        val resultado = ValidadorDocumento.validar(
            nombre = "",
            tipo = "Contrato"
        )

        assertEquals(
            "El nombre del documento es obligatorio.",
            resultado
        )
    }

    @Test
    fun nombreCorto_muestraError() {
        val resultado = ValidadorDocumento.validar(
            nombre = "AB",
            tipo = "Contrato"
        )

        assertEquals(
            "El nombre debe tener al menos 3 caracteres.",
            resultado
        )
    }

    @Test
    fun tipoVacio_muestraError() {
        val resultado = ValidadorDocumento.validar(
            nombre = "Contrato de trabajo",
            tipo = ""
        )

        assertEquals(
            "El tipo de documento es obligatorio.",
            resultado
        )
    }

    @Test
    fun datosValidos_permitenGuardar() {
        val resultado = ValidadorDocumento.validar(
            nombre = "Contrato de trabajo",
            tipo = "Contrato"
        )

        assertNull(resultado)
    }

    @Test
    fun espaciosEnBlanco_seConsideranVacios() {
        val resultado = ValidadorDocumento.validar(
            nombre = "   ",
            tipo = "Contrato"
        )

        assertEquals(
            "El nombre del documento es obligatorio.",
            resultado
        )
    }
}
