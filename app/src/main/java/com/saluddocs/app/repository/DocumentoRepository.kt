package com.saluddocs.app.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DocumentoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

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
}
