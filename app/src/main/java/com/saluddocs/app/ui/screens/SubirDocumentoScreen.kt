package com.saluddocs.app.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saluddocs.app.ui.components.SaludDocsButton

@Composable
fun SubirDocumentoScreen(
    onVolver: () -> Unit
) {
    var archivoSeleccionado by remember {
        mutableStateOf<Uri?>(null)
    }

    var mensaje by remember {
        mutableStateOf("")
    }

    val selectorArchivo = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        archivoSeleccionado = uri
        mensaje = if (uri != null) {
            "Archivo seleccionado correctamente"
        } else {
            "No se seleccionó ningún archivo"
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
            text = "Subir documento",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Selecciona un archivo PDF o una imagen")

        Spacer(modifier = Modifier.height(24.dp))

        SaludDocsButton(
            texto = "Seleccionar archivo",
            onClick = {
                selectorArchivo.launch("*/*")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (archivoSeleccionado != null) {
            Text("Archivo seleccionado")

            Spacer(modifier = Modifier.height(16.dp))

            SaludDocsButton(
                texto = "Guardar documento",
                onClick = {
                    mensaje = "Carga simulada correctamente"
                }
            )
        }

        if (mensaje.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(mensaje)
        }

        Spacer(modifier = Modifier.height(32.dp))

        SaludDocsButton(
            texto = "Volver al menú",
            onClick = onVolver
        )
    }
}
