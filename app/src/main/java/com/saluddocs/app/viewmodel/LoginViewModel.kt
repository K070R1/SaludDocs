package com.saluddocs.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    var correo by mutableStateOf("")
        private set

    var contrasena by mutableStateOf("")
        private set

    var mensaje by mutableStateOf("")
        private set

    fun actualizarCorreo(nuevoCorreo: String) {
        correo = nuevoCorreo
    }

    fun actualizarContrasena(nuevaContrasena: String) {
        contrasena = nuevaContrasena
    }

    fun iniciarSesion() {
        mensaje = if (correo.isBlank() || contrasena.isBlank()) {
            "Completa todos los campos"
        } else {
            "Datos ingresados correctamente"
        }
    }
}
