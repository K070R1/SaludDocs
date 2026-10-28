package com.saluddocs.app.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UsuarioRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun obtenerRol(
        onResultado: (String?, String) -> Unit
    ) {
        val usuarioId = auth.currentUser?.uid

        if (usuarioId == null) {
            onResultado(null, "Debes iniciar sesión")
            return
        }

        db.collection("usuarios")
            .document(usuarioId)
            .get()
            .addOnSuccessListener { documento ->

                if (documento.exists()) {
                    val rol = documento.getString("rol")

                    if (rol == "funcionario" || rol == "administrador") {
                        onResultado(rol, "")
                    } else {
                        onResultado(null, "El usuario no tiene un rol válido")
                    }
                } else {
                    onResultado(null, "No se encontró el perfil del usuario")
                }
            }
            .addOnFailureListener { error ->
                onResultado(
                    null,
                    "Error al consultar el rol: ${error.message}"
                )
            }
    }
}

