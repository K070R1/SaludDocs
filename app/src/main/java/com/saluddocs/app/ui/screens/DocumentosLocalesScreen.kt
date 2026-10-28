package com.saluddocs.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.saluddocs.app.data.local.DocumentoLocal
import com.saluddocs.app.viewmodel.DocumentoLocalViewModel

private val AzulSaludDocs = Color(0xFF1769AA)
private val FondoSaludDocs = Color(0xFFF5F8FC)
private val TextoPrincipal = Color(0xFF17324D)
private val TextoSecundario = Color(0xFF64748B)
private val RojoEliminar = Color(0xFFB42318)

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
        containerColor = FondoSaludDocs,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Documentos locales",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulSaludDocs
                )
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Text(
                    text = "Gestiona tus documentos guardados en el dispositivo",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario
                )
            }

            // FORMULARIO
            item {
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
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = if (documentoEnEdicion == null) {
                                    Icons.Default.Add
                                } else {
                                    Icons.Default.Edit
                                },
                                contentDescription = null,
                                tint = AzulSaludDocs
                            )

                            Text(
                                text = if (documentoEnEdicion == null) {
                                    "Registrar documento"
                                } else {
                                    "Editar documento"
                                },
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextoPrincipal
                            )
                        }

                        Text(
                            text = "Completa los campos obligatorios (*)",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextoSecundario
                        )

                        OutlinedTextField(
                            value = nombre,
                            onValueChange = viewModel::actualizarNombre,
                            label = {
                                Text("Nombre del documento *")
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AzulSaludDocs,
                                focusedLabelColor = AzulSaludDocs,
                                cursorColor = AzulSaludDocs
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = tipo,
                            onValueChange = viewModel::actualizarTipo,
                            label = {
                                Text("Tipo de documento *")
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AzulSaludDocs,
                                focusedLabelColor = AzulSaludDocs,
                                cursorColor = AzulSaludDocs
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = descripcion,
                            onValueChange = viewModel::actualizarDescripcion,
                            label = {
                                Text("Descripción (opcional)")
                            },
                            minLines = 2,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AzulSaludDocs,
                                focusedLabelColor = AzulSaludDocs,
                                cursorColor = AzulSaludDocs
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = viewModel::guardarDocumento,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AzulSaludDocs
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = null
                            )

                            Spacer(Modifier.width(8.dp))

                            Text(
                                text = if (documentoEnEdicion == null) {
                                    "Guardar documento"
                                } else {
                                    "Actualizar documento"
                                },
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        if (documentoEnEdicion != null) {
                            OutlinedButton(
                                onClick = viewModel::cancelarEdicion,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Cancelar edición",
                                    color = AzulSaludDocs
                                )
                            }
                        }

                        if (mensaje.isNotBlank()) {
                            val esExito = mensaje.contains(
                                "correctamente",
                                ignoreCase = true
                            )

                            Surface(
                                color = if (esExito) {
                                    Color(0xFFE8F5E9)
                                } else {
                                    Color(0xFFFFEBEE)
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = mensaje,
                                    modifier = Modifier.padding(12.dp),
                                    color = if (esExito) {
                                        Color(0xFF2E7D32)
                                    } else {
                                        RojoEliminar
                                    },
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }

            // ENCABEZADO DEL LISTADO
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Documentos registrados",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextoPrincipal
                    )

                    Surface(
                        color = Color(0xFFE3F0FA),
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Text(
                            text = "${documentos.size}",
                            modifier = Modifier.padding(
                                horizontal = 14.dp,
                                vertical = 6.dp
                            ),
                            color = AzulSaludDocs,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (documentos.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                tint = AzulSaludDocs,
                                modifier = Modifier.size(42.dp)
                            )

                            Text(
                                text = "No hay documentos registrados",
                                fontWeight = FontWeight.SemiBold,
                                color = TextoPrincipal
                            )

                            Text(
                                text = "Completa el formulario para agregar tu primer documento.",
                                color = TextoSecundario,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            // LISTADO DE DOCUMENTOS
            items(
                items = documentos,
                key = { it.id }
            ) { documento ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                tint = AzulSaludDocs
                            )

                            Text(
                                text = documento.nombre,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextoPrincipal,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Text(
                            text = "Tipo: ${documento.tipo}",
                            color = TextoSecundario
                        )

                        if (documento.descripcion.isNotBlank()) {
                            Text(
                                text = "Descripción: ${documento.descripcion}",
                                color = TextoSecundario
                            )
                        }

                        Surface(
                            color = if (
                                documento.estado.equals(
                                    "Pendiente",
                                    ignoreCase = true
                                )
                            ) {
                                Color(0xFFFFF3CD)
                            } else {
                                Color(0xFFE3F0FA)
                            },
                            shape = RoundedCornerShape(50.dp)
                        ) {
                            Text(
                                text = documento.estado,
                                modifier = Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 6.dp
                                ),
                                color = if (
                                    documento.estado.equals(
                                        "Pendiente",
                                        ignoreCase = true
                                    )
                                ) {
                                    Color(0xFF856404)
                                } else {
                                    AzulSaludDocs
                                },
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        HorizontalDivider(
                            color = Color(0xFFE2E8F0)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    viewModel.editarDocumento(documento)
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = AzulSaludDocs
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )

                                Spacer(Modifier.width(6.dp))

                                Text("Editar")
                            }

                            OutlinedButton(
                                onClick = {
                                    documentoParaEliminar = documento
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = RojoEliminar
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )

                                Spacer(Modifier.width(6.dp))

                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }

    // CONFIRMACIÓN DE ELIMINACIÓN
    documentoParaEliminar?.let { documento ->
        AlertDialog(
            onDismissRequest = {
                documentoParaEliminar = null
            },
            title = {
                Text(
                    text = "Eliminar documento",
                    fontWeight = FontWeight.Bold
                )
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
                    Text(
                        text = "Eliminar",
                        color = RojoEliminar
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        documentoParaEliminar = null
                    }
                ) {
                    Text(
                        text = "Cancelar",
                        color = AzulSaludDocs
                    )
                }
            },
            shape = RoundedCornerShape(18.dp)
        )
    }
}
