package com.saluddocs.app.data.repository

import com.saluddocs.app.data.local.DocumentoLocal
import com.saluddocs.app.data.local.DocumentoLocalDao
import kotlinx.coroutines.flow.Flow

class DocumentoLocalRepository(
    private val documentoLocalDao: DocumentoLocalDao
) {

    // CREATE: insertar un documento
    suspend fun insertar(
        documento: DocumentoLocal
    ) {
        documentoLocalDao.insertar(documento)
    }

    // READ: obtener todos los documentos
    fun obtenerTodos(): Flow<List<DocumentoLocal>> {
        return documentoLocalDao.obtenerTodos()
    }

    // READ: obtener un documento por ID
    suspend fun obtenerPorId(
        id: Int
    ): DocumentoLocal? {
        return documentoLocalDao.obtenerPorId(id)
    }

    // UPDATE: actualizar un documento
    suspend fun actualizar(
        documento: DocumentoLocal
    ) {
        documentoLocalDao.actualizar(documento)
    }

    // DELETE: eliminar un documento
    suspend fun eliminar(
        documento: DocumentoLocal
    ) {
        documentoLocalDao.eliminar(documento)
    }
}
