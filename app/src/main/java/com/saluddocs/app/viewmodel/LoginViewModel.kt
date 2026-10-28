package com.saluddocs.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    var correo by mutableStateOf("")
        private set

    var contrasena by mutableStateOf("")
        private set

    var mensaje by mutableStateOf("")
        private set

    var cargando by mutableStateOf(false)
        private set

    fun actualizarCorreo(nuevoCorreo: String) {
        correo = nuevoCorreo
    }

    fun actualizarContrasena(nuevaContrasena: String) {
        contrasena = nuevaContrasena
    }

    fun iniciarSesion(onLoginExitoso: () -> Unit) {

        if (correo.isBlank() || contrasena.isBlank()) {
            mensaje = "Completa todos los campos"
            return
        }

        if (cargando) return

        cargando = true
        mensaje = "Verificando credenciales..."

        auth.signInWithEmailAndPassword(
            correo.trim(),
            contrasena
        ).addOnCompleteListener { tarea ->

            cargando = false

            if (tarea.isSuccessful) {
                mensaje = "Inicio de sesión exitoso"
                onLoginExitoso()
            } else {
                mensaje = "Correo o contraseña incorrectos"
            }
        }
    }
}
