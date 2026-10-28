package com.saluddocs.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.saluddocs.app.ui.screens.DocumentosScreen
import com.saluddocs.app.ui.screens.HomeScreen
import com.saluddocs.app.ui.screens.LoginScreen

@Composable
fun AppNavigation() {
    var pantallaActual by rememberSaveable {
        mutableStateOf("login")
    }

    when (pantallaActual) {
        "login" -> LoginScreen(
            onLoginExitoso = {
                pantallaActual = "home"
            }
        )

        "home" -> HomeScreen(
            onCerrarSesion = {
                pantallaActual = "login"
            },
            onMisDocumentos = {
                pantallaActual = "documentos"
            }
        )

        "documentos" -> DocumentosScreen(
            onVolver = {
                pantallaActual = "home"
            }
        )
    }
}



