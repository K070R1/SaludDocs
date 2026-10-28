package com.saluddocs.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saluddocs.app.repository.UsuarioRepository
import com.saluddocs.app.ui.components.SaludDocsButton

@Composable
fun HomeScreen(
    onCerrarSesion: () -> Unit,
    onMisDocumentos: () -> Unit,
    onSubirDocumento: () -> Unit,
    onRevisarDocumentos: () -> Unit
) {
    val repository = remember { UsuarioRepository() }

    var rol by remember {
        mutableStateOf<String?>(null)
    }

    var cargando by remember {
        mutableStateOf(true)
    }

    var mensaje by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        repository.obtenerRol { rolObtenido, error ->
            rol = rolObtenido
            mensaje = error
            cargando = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bienvenido a SaludDocs",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Sistema de gestión documental")

        Spacer(modifier = Modifier.height(16.dp))

        if (cargando) {
            CircularProgressIndicator()
        } else if (mensaje.isNotEmpty()) {
            Text(mensaje)
        } else {
            Text(
                text = if (rol == "administrador") {
                    "Sesión: Administrador"
                } else {
                    "Sesión: Funcionario"
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (rol == "funcionario") {
                SaludDocsButton(
                    texto = "Mis documentos",
                    onClick = onMisDocumentos
                )

                Spacer(modifier = Modifier.height(12.dp))

                SaludDocsButton(
                    texto = "Subir documento",
                    onClick = onSubirDocumento
                )
            }

            if (rol == "administrador") {
                SaludDocsButton(
                    texto = "Mis documentos",
                    onClick = onMisDocumentos
                )

                Spacer(modifier = Modifier.height(12.dp))

                SaludDocsButton(
                    texto = "Revisar documentos",
                    onClick = onRevisarDocumentos
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SaludDocsButton(
            texto = "Cerrar sesión",
            onClick = onCerrarSesion
        )
    }
}
