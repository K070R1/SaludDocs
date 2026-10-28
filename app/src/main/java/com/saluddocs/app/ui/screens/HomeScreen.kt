package com.saluddocs.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saluddocs.app.ui.components.SaludDocsButton

@Composable
fun HomeScreen(
    onCerrarSesion: () -> Unit,
    onMisDocumentos: () -> Unit
) {
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

        Spacer(modifier = Modifier.height(32.dp))

        SaludDocsButton(
            texto = "Mis documentos",
            onClick = onMisDocumentos
        )

        Spacer(modifier = Modifier.height(12.dp))

        SaludDocsButton(
            texto = "Subir documento",
            onClick = {
                // Próximamente: carga de archivos
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        SaludDocsButton(
            texto = "Cerrar sesión",
            onClick = onCerrarSesion
        )
    }
}
