package com.saluddocs.app.ui.screens

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.saluddocs.app.repository.DocumentoRepository

private val AzulSubir = Color(0xFF1769AA)
private val AzulClaroSubir = Color(0xFFE4F1FF)
private val FondoSubir = Color(0xFFF5F8FC)
private val TextoSubir = Color(0xFF203248)
private val TextoSecundarioSubir = Color(0xFF64748B)

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
            .background(FondoSubir)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            // Encabezado azul
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AzulSubir
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.UploadFile,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )

                        Text(
                            text = "Subir documento",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Text(
                        text = "Registra un documento para su revisión.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFD9EDFF)
                    )
                }
            }

            // Tarjeta de selección
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .background(
                                AzulClaroSubir,
                                RoundedCornerShape(20.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = AzulSubir,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Text(
                        text = "Selecciona tu documento PDF",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextoSubir
                    )

                    Text(
                        text = "Busca un archivo PDF en tu dispositivo para registrar sus datos.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoSecundarioSubir
                    )

                    // Archivo seleccionado
                    if (archivoSeleccionado != null) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = FondoSubir,
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Description,
                                    contentDescription = null,
                                    tint = AzulSubir
                                )

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = nombreArchivo.ifBlank {
                                            "Archivo sin nombre"
                                        },
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = TextoSubir,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Text(
                                        text = "Archivo seleccionado",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextoSecundarioSubir
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF15803D)
                                )
                            }
                        }
                    }

                    // Seleccionar archivo
                    Button(
                        onClick = {
                            if (!guardando) {
                                selectorArchivo.launch("application/pdf")
                            }
                        },
                        enabled = !guardando,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AzulSubir
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.FolderOpen,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text("Seleccionar archivo")
                    }

                    // Guardar en Firestore
                    if (archivoSeleccionado != null) {
                        Button(
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
                            },
                            enabled = !guardando,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AzulClaroSubir,
                                contentColor = AzulSubir
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text("Guardar documento")
                        }
                    }

                    if (guardando) {
                        CircularProgressIndicator(
                            color = AzulSubir
                        )
                    }

                    if (mensaje.isNotEmpty()) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = FondoSubir,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = mensaje,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextoSubir,
                                modifier = Modifier.padding(14.dp)
                            )
                        }
                    }
                }
            }

            // Aviso importante
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = TextoSecundarioSubir
                )

                Text(
                    text = "Por ahora se registra el nombre y tipo del documento " +
                            "en Firestore. El archivo PDF todavía no se almacena.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundarioSubir
                )
            }
        }

        // Volver al menú
        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = AzulSubir
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text("Volver al menú")
        }
    }
}
