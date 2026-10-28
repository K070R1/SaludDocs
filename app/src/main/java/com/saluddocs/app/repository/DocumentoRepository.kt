package com.saluddocs.app.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DocumentoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Registrar un documento
    fun guardarDocumento(
        nombre: String,
        tipo: String,
        onResultado: (Boolean, String) -> Unit
    ) {
        val usuarioId = auth.currentUser?.uid

        if (usuarioId == null) {
            onResultado(false, "Debes iniciar sesión")
            return
        }

        val documento = hashMapOf(
            "nombre" to nombre,
            "tipo" to tipo,
            "estado" to "Pendiente",
            "fechaCarga" to com.google.firebase.Timestamp.now(),
            "usuarioId" to usuarioId
        )

        db.collection("documentos")
            .add(documento)
            .addOnSuccessListener {
                onResultado(true, "Documento registrado correctamente")
            }
            .addOnFailureListener {
                onResultado(false, "Error al guardar: ${it.message}")
            }
    }

    // Consultar documentos del usuario autenticado
    fun obtenerDocumentos(
        onResultado: (List<Map<String, Any>>, String) -> Unit
    ) {
        val usuarioId = auth.currentUser?.uid

        if (usuarioId == null) {
            onResultado(emptyList(), "Debes iniciar sesión")
            return
        }

        db.collection("documentos")
            .whereEqualTo("usuarioId", usuarioId)
            .get()
            .addOnSuccessListener { resultado ->
                val documentos = resultado.documents.mapNotNull { documento ->
                    documento.data
                }

                onResultado(documentos, "")
            }
            .addOnFailureListener { error ->
                onResultado(
                    emptyList(),
                    "Error al cargar documentos: ${error.message}"
                )
            }
    }

    // Consultar documentos pendientes para revisión
    fun obtenerDocumentosPendientes(
        onResultado: (List<Map<String, Any>>, String) -> Unit
    ) {
        if (auth.currentUser == null) {
            onResultado(emptyList(), "Debes iniciar sesión")
            return
        }

        db.collection("documentos")
            .whereEqualTo("estado", "Pendiente")
            .get()
            .addOnSuccessListener { resultado ->
                val documentos = resultado.documents.mapNotNull { documento ->
                    documento.data?.plus(
                        "id" to documento.id
                    )
                }

                onResultado(documentos, "")
            }
            .addOnFailureListener { error ->
                onResultado(
                    emptyList(),
                    "Error al cargar documentos pendientes: ${error.message}"
                )
            }
    }

    // Aprobar o rechazar un documento
    fun actualizarEstadoDocumento(
        documentoId: String,
        nuevoEstado: String,
        onResultado: (Boolean, String) -> Unit
    ) {
        if (auth.currentUser == null) {
            onResultado(false, "Debes iniciar sesión")
            return
        }

        if (nuevoEstado != "Aprobado" && nuevoEstado != "Rechazado") {
            onResultado(false, "Estado no válido")
            return
        }

        db.collection("documentos")
            .document(documentoId)
            .update("estado", nuevoEstado)
            .addOnSuccessListener {
                onResultado(
                    true,
                    "Documento ${nuevoEstado.lowercase()} correctamente"
                )
            }
            .addOnFailureListener { error ->
                onResultado(
                    false,
                    "No se pudo actualizar el documento: ${error.message}"
                )
            }
    }

}

