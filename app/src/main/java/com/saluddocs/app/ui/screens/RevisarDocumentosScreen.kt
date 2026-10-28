package com.saluddocs.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.saluddocs.app.repository.DocumentoRepository

private val AzulRevision = Color(0xFF1769AA)
private val AzulClaroRevision = Color(0xFFE4F1FF)
private val FondoRevision = Color(0xFFF5F8FC)
private val TextoRevision = Color(0xFF203248)
private val TextoSecundarioRevision = Color(0xFF64748B)
private val VerdeRevision = Color(0xFF176B35)
private val RojoRevision = Color(0xFFB3261E)

@Composable
fun RevisarDocumentosScreen(
    onVolver: () -> Unit
) {
    val repository = remember { DocumentoRepository() }

    var documentos by remember {
        mutableStateOf<List<Map<String, Any>>>(emptyList())
    }

    var cargando by remember {
        mutableStateOf(true)
    }

    var procesando by remember {
        mutableStateOf(false)
    }

    var mensaje by remember {
        mutableStateOf("")
    }

    var error by remember {
        mutableStateOf("")
    }

    // Cargar documentos pendientes desde Firestore
    fun cargarDocumentos() {
        cargando = true

        repository.obtenerDocumentosPendientes { resultado, errorCarga ->
            documentos = resultado
            error = errorCarga
            cargando = false
        }
    }

    // Aprobar o rechazar un documento
    fun cambiarEstado(
        documentoId: String,
        nuevoEstado: String
    ) {
        procesando = true
        mensaje = ""
        error = ""

        repository.actualizarEstadoDocumento(
            documentoId = documentoId,
            nuevoEstado = nuevoEstado
        ) { exito, respuesta ->
            procesando = false

            if (exito) {
                mensaje = respuesta
                cargarDocumentos()
            } else {
                error = respuesta
            }
        }
    }

    LaunchedEffect(Unit) {
        cargarDocumentos()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoRevision)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Encabezado azul
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = AzulRevision
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
                        imageVector = Icons.Default.FactCheck,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )

                    Text(
                        text = "Revisar documentos",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Text(
                    text = "Gestiona las solicitudes pendientes de revisión.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFD9EDFF)
                )
            }
        }

        // Contador de documentos
        if (!cargando && error.isEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Documentos pendientes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextoRevision
                )

                Surface(
                    color = Color(0xFFFFF0C2),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text(
                        text = "${documentos.size} pendientes",
                        color = Color(0xFF805500),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 7.dp
                        )
                    )
                }
            }
        }

        // Mensaje de éxito
        if (mensaje.isNotEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFDDF5E3),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = mensaje,
                    color = VerdeRevision,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(14.dp)
                )
            }
        }

        // Mensaje de error
        if (error.isNotEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFFFE0E0),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = error,
                    color = RojoRevision,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(14.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when {
                cargando -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = AzulRevision
                    )
                }

                error.isNotEmpty() -> {
                    // El mensaje de error ya se muestra arriba
                }

                documentos.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = AzulRevision,
                            modifier = Modifier.size(42.dp)
                        )

                        Text(
                            text = "No hay documentos pendientes",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextoSecundarioRevision
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 8.dp)
                    ) {
                        items(
                            items = documentos,
                            key = { it["id"].toString() }
                        ) { documento ->

                            val documentoId = documento["id"]?.toString() ?: ""
                            val nombre = documento["nombre"]?.toString()
                                ?: "Sin nombre"
                            val tipo = documento["tipo"]?.toString()
                                ?: "Sin tipo"
                            val estado = documento["estado"]?.toString()
                                ?: "Sin estado"
                            val usuarioId = documento["usuarioId"]?.toString()
                                ?: "Desconocido"

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 3.dp
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(14.dp)
                                ) {

                                    // Información del documento
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(48.dp)
                                                .background(
                                                    AzulClaroRevision,
                                                    RoundedCornerShape(12.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Description,
                                                contentDescription = null,
                                                tint = AzulRevision,
                                                modifier = Modifier.size(26.dp)
                                            )
                                        }

                                        Column(
                                            modifier = Modifier.weight(1f),
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = nombre,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = TextoRevision,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )

                                            Text(
                                                text = "Tipo: $tipo",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = TextoSecundarioRevision
                                            )
                                        }
                                    }

                                    // Estado pendiente
                                    EstadoDocumento(estado)

                                    Text(
                                        text = "Usuario: $usuarioId",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextoSecundarioRevision,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    HorizontalDivider(
                                        color = Color(0xFFE2E8F0)
                                    )

                                    // Botones de revisión
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Button(
                                            onClick = {
                                                cambiarEstado(
                                                    documentoId,
                                                    "Aprobado"
                                                )
                                            },
                                            enabled = !procesando && documentoId.isNotEmpty(),
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFFDDF5E3),
                                                contentColor = VerdeRevision
                                            )
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp)
                                            )

                                            Spacer(modifier = Modifier.width(4.dp))

                                            Text("Aprobar")
                                        }

                                        Button(
                                            onClick = {
                                                cambiarEstado(
                                                    documentoId,
                                                    "Rechazado"
                                                )
                                            },
                                            enabled = !procesando && documentoId.isNotEmpty(),
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFFFFE0E0),
                                                contentColor = RojoRevision
                                            )
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp)
                                            )

                                            Spacer(modifier = Modifier.width(4.dp))

                                            Text("Rechazar")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Volver al menú
        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = AzulRevision
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
