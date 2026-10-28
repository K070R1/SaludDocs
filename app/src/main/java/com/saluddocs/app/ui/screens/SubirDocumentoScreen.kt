package com.saluddocs.app.ui.screens

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.saluddocs.app.repository.DocumentoRepository
import com.saluddocs.app.ui.components.SaludDocsButton

@Composable
fun SubirDocumentoScreen(
    onVolver: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember { DocumentoRepository() }

    var archivoSeleccionado by remember {
        mutableStateOf<Uri?>(null)
    }

    var nombreArchivo by remember {
        mutableStateOf("")
    }

    var mensaje by remember {
        mutableStateOf("")
    }

    var guardando by remember {
        mutableStateOf(false)
    }

    val selectorArchivo = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->

        archivoSeleccionado = uri
        nombreArchivo = ""

        if (uri != null) {
            context.contentResolver.query(
                uri,
                arrayOf(OpenableColumns.DISPLAY_NAME),
                null,
                null,
                null
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    nombreArchivo = cursor.getString(0) ?: ""
                }
            }

            mensaje = "Archivo seleccionado correctamente"
        } else {
            mensaje = "No se seleccionó ningún archivo"
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

        Text("Selecciona un archivo PDF")

        Spacer(modifier = Modifier.height(24.dp))

        SaludDocsButton(
            texto = "Seleccionar archivo",
            onClick = {
                if (!guardando) {
                    selectorArchivo.launch("application/pdf")
                }
            }
        )

        if (archivoSeleccionado != null) {

            Spacer(modifier = Modifier.height(16.dp))

            Text("Archivo: $nombreArchivo")

            Spacer(modifier = Modifier.height(16.dp))

            SaludDocsButton(
                texto = "Guardar documento",
                onClick = {
                    if (!guardando) {
                        if (nombreArchivo.isBlank()) {
                            mensaje = "No se pudo obtener el nombre del archivo"
                        } else {
                            guardando = true
                            mensaje = "Registrando documento..."

                            repository.guardarDocumento(
                                nombre = nombreArchivo,
                                tipo = "PDF"
                            ) { exitoso, respuesta ->
                                guardando = false

                                mensaje = if (exitoso) {
                                    "Registro guardado en Firestore. " +
                                            "El PDF todavía no se ha subido."
                                } else {
                                    respuesta
                                }
                            }
                        }
                    }
                }
            )
        }

        if (guardando) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
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
