package com.saluddocs.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentoLocalDao {

    // CREATE: guardar un documento
    @Insert
    suspend fun insertar(documento: DocumentoLocal)

    // READ: consultar todos los documentos
    @Query("SELECT * FROM documentos_locales ORDER BY fechaRegistro DESC")
    fun obtenerTodos(): Flow<List<DocumentoLocal>>

    // READ: buscar un documento por su ID
    @Query("SELECT * FROM documentos_locales WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): DocumentoLocal?

    // UPDATE: modificar un documento existente
    @Update
    suspend fun actualizar(documento: DocumentoLocal)

    // DELETE: eliminar un documento
    @Delete
    suspend fun eliminar(documento: DocumentoLocal)
}
