package com.saluddocs.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "documentos_locales")
data class DocumentoLocal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,

    val tipo: String,

    val descripcion: String,

    val estado: String = "Pendiente",

    val fechaRegistro: Long = System.currentTimeMillis()
)
