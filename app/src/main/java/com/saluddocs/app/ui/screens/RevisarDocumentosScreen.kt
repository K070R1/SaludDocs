package com.saluddocs.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saluddocs.app.repository.DocumentoRepository
import com.saluddocs.app.ui.components.SaludDocsButton

@Composable
fun RevisarDocumentosScreen(
    onVolver: () -> Unit
) {
    val repository = remember { DocumentoRepository() }

    var documentos by remember {
        mutableStateOf<List<Map<String, Any>>>(emptyList())
    }

    var cargando by remember { mutableStateOf(true) }
    var procesando by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    // Cargar los documentos pendientes
    fun cargarDocumentos() {
        cargando = true

        repository.obtenerDocumentosPendientes { resultado, errorCarga ->
            documentos = resultado
            error = errorCarga
            cargando = false
        }
    }

    // Aprobar o rechazar un documento
    fun cambiarEstado(documentoId: String, nuevoEstado: String) {
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
            .padding(24.dp)
    ) {
        Text(
            text = "Revisar documentos",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (mensaje.isNotEmpty()) {
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        when {
            cargando -> {
                CircularProgressIndicator()
            }

            documentos.isEmpty() -> {
                Text("No hay documentos pendientes")
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = documentos,
                        key = { it["id"].toString() }
                    ) { documento ->

                        val documentoId = documento["id"]?.toString() ?: ""

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = documento["nombre"]
                                        ?.toString() ?: "Sin nombre",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text("Tipo: ${documento["tipo"] ?: "Sin tipo"}")
                                Text("Estado: ${documento["estado"] ?: "Sin estado"}")
                                Text("Usuario: ${documento["usuarioId"] ?: "Desconocido"}")

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            cambiarEstado(documentoId, "Aprobado")
                                        },
                                        enabled = !procesando && documentoId.isNotEmpty(),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Aprobar")
                                    }

                                    OutlinedButton(
                                        onClick = {
                                            cambiarEstado(documentoId, "Rechazado")
                                        },
                                        enabled = !procesando && documentoId.isNotEmpty(),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Rechazar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        SaludDocsButton(
            texto = "Volver al menú",
            onClick = onVolver
        )
    }
}
