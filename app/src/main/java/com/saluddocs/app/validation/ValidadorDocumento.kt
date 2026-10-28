package com.saluddocs.app.validation

object ValidadorDocumento {

    fun validar(
        nombre: String,
        tipo: String
    ): String? {

        val nombreLimpio = nombre.trim()
        val tipoLimpio = tipo.trim()

        if (nombreLimpio.isEmpty()) {
            return "El nombre del documento es obligatorio."
        }

        if (tipoLimpio.isEmpty()) {
            return "El tipo de documento es obligatorio."
        }

        if (nombreLimpio.length < 3) {
            return "El nombre debe tener al menos 3 caracteres."
        }

        // Null significa que la validación fue exitosa
        return null
    }
}
