package com.saluddocs.app.model

data class Documento(
    val id: Int,
    val nombre: String,
    val tipo: String,
    val fechaCarga: String,
    val estado: String
)
