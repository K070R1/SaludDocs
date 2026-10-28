package com.saluddocs.app.navigation

import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.saluddocs.app.ui.screens.DocumentosScreen
import com.saluddocs.app.ui.screens.DocumentosLocalesScreen
import com.saluddocs.app.ui.screens.HomeScreen
import com.saluddocs.app.ui.screens.LoginScreen
import com.saluddocs.app.ui.screens.SubirDocumentoScreen
import com.saluddocs.app.ui.screens.RevisarDocumentosScreen

@Composable
fun AppNavigation() {

    var pantallaActual by rememberSaveable {
        mutableStateOf("login")
    }

    when (pantallaActual) {

        // Inicio de sesión
        "login" -> LoginScreen(
            onLoginExitoso = {
                pantallaActual = "home"
            }
        )

        // Menú principal
        "home" -> HomeScreen(
            onCerrarSesion = {
                FirebaseAuth.getInstance().signOut()
                pantallaActual = "login"
            },
            onMisDocumentos = {
                pantallaActual = "documentos"
            },
            onSubirDocumento = {
                pantallaActual = "subir"
            },
            onRevisarDocumentos = {
                pantallaActual = "revisar"
            },
            onDocumentosLocales = {
                pantallaActual = "documentos_locales"
            }
        )

        // Documentos almacenados en Firebase
        "documentos" -> DocumentosScreen(
            onVolver = {
                pantallaActual = "home"
            }
        )

        // Registro de documentos en Firebase
        "subir" -> SubirDocumentoScreen(
            onVolver = {
                pantallaActual = "home"
            }
        )

        // Revisión de documentos por administrador
        "revisar" -> RevisarDocumentosScreen(
            onVolver = {
                pantallaActual = "home"
            }
        )

        // Nuevo CRUD local con Room
        "documentos_locales" -> DocumentosLocalesScreen(
            onVolver = {
                pantallaActual = "home"
            }
        )
    }
}
