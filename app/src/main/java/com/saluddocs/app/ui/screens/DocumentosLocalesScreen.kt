package com.saluddocs.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.saluddocs.app.data.local.DocumentoLocal
import com.saluddocs.app.viewmodel.DocumentoLocalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentosLocalesScreen(
    onVolver: () -> Unit,
    viewModel: DocumentoLocalViewModel = viewModel()
) {
    val documentos by viewModel.documentos.collectAsState()
    val nombre by viewModel.nombre.collectAsState()
    val tipo by viewModel.tipo.collectAsState()
    val descripcion by viewModel.descripcion.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val documentoEnEdicion by viewModel.documentoEnEdicion.collectAsState()

    var documentoParaEliminar by remember {
        mutableStateOf<DocumentoLocal?>(null)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Documentos locales") },
                navigationIcon = {
                    TextButton(onClick = onVolver) {
                        Text("Volver")
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = if (documentoEnEdicion == null) {
                        "Registrar documento"
                    } else {
                        "Editar documento"
                    },
                    style = MaterialTheme.typography.titleLarge
                )
            }

            item {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = viewModel::actualizarNombre,
                    label = { Text("Nombre del documento *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = tipo,
                    onValueChange = viewModel::actualizarTipo,
                    label = { Text("Tipo de documento *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = viewModel::actualizarDescripcion,
                    label = { Text("Descripción") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Button(
                    onClick = viewModel::guardarDocumento,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (documentoEnEdicion == null) {
                            "Guardar documento"
                        } else {
                            "Actualizar documento"
                        }
                    )
                }
            }

            if (documentoEnEdicion != null) {
                item {
                    OutlinedButton(
                        onClick = viewModel::cancelarEdicion,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancelar edición")
                    }
                }
            }

            if (mensaje.isNotBlank()) {
                item {
                    Text(
                        text = mensaje,
                        color = if (
                            mensaje.contains("correctamente")
                        ) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.error
                        }
                    )
                }
            }

            item {
                HorizontalDivider()

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Documentos registrados (${documentos.size})",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (documentos.isEmpty()) {
                item {
                    Text("Todavía no hay documentos locales registrados.")
                }
            }

            items(
                items = documentos,
                key = { it.id }
            ) { documento ->

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = documento.nombre,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text("Tipo: ${documento.tipo}")

                        if (documento.descripcion.isNotBlank()) {
                            Text("Descripción: ${documento.descripcion}")
                        }

                        Text("Estado: ${documento.estado}")

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    viewModel.editarDocumento(documento)
                                }
                            ) {
                                Text("Editar")
                            }

                            TextButton(
                                onClick = {
                                    documentoParaEliminar = documento
                                }
                            ) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }

    documentoParaEliminar?.let { documento ->
        AlertDialog(
            onDismissRequest = {
                documentoParaEliminar = null
            },
            title = {
                Text("Eliminar documento")
            },
            text = {
                Text(
                    "¿Seguro que deseas eliminar '${documento.nombre}'?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.eliminarDocumento(documento)
                        documentoParaEliminar = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        documentoParaEliminar = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
