package com.saluddocs.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.saluddocs.app.repository.DocumentoRepository
import java.text.SimpleDateFormat
import java.util.Locale

private val AzulDocumentos = Color(0xFF1769AA)
private val AzulClaroDocumentos = Color(0xFFE4F1FF)
private val FondoDocumentos = Color(0xFFF5F8FC)
private val TextoDocumentos = Color(0xFF203248)
private val TextoSecundarioDocumentos = Color(0xFF64748B)

@Composable
fun DocumentosScreen(
    onVolver: () -> Unit
) {
    val repository = remember { DocumentoRepository() }

    var documentoSeleccionado by remember {
        mutableStateOf<Map<String, Any>?>(null)
    }

    var documentos by remember {
        mutableStateOf<List<Map<String, Any>>>(emptyList())
    }

    var busqueda by remember {
        mutableStateOf("")
    }

    var cargando by remember {
        mutableStateOf(true)
    }

    var mensaje by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        repository.obtenerDocumentos { resultado, error ->
            documentos = resultado
            mensaje = error
            cargando = false
        }
    }

    val documentosFiltrados = documentos.filter { documento ->
        val nombre = documento["nombre"] as? String ?: ""
        val tipo = documento["tipo"] as? String ?: ""
        val estado = documento["estado"] as? String ?: ""

        nombre.contains(busqueda, ignoreCase = true) ||
                tipo.contains(busqueda, ignoreCase = true) ||
                estado.contains(busqueda, ignoreCase = true)
    }

    // Conservar la navegación al detalle
    val seleccionado = documentoSeleccionado

    if (seleccionado != null) {
        DetalleDocumentoScreen(
            documento = seleccionado,
            onVolver = {
                documentoSeleccionado = null
            }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoDocumentos)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Encabezado azul
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = AzulDocumentos
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
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )

                    Text(
                        text = "Mis documentos",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Text(
                    text = "Consulta y revisa tus documentos registrados.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFD9EDFF)
                )
            }
        }

        // Buscador
        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            placeholder = {
                Text("Nombre, tipo o estado")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = AzulDocumentos
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AzulDocumentos,
                unfocusedBorderColor = Color(0xFFD7E2EE),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Contador
        if (!cargando && mensaje.isEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Documentos registrados",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextoDocumentos
                )

                Surface(
                    color = AzulClaroDocumentos,
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text(
                        text = "${documentosFiltrados.size} documentos",
                        color = AzulDocumentos,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 7.dp
                        )
                    )
                }
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
                        color = AzulDocumentos
                    )
                }

                mensaje.isNotEmpty() -> {
                    Text(
                        text = mensaje,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                documentosFiltrados.isEmpty() -> {
                    Text(
                        text = if (busqueda.isBlank()) {
                            "Todavía no tienes documentos registrados"
                        } else {
                            "No se encontraron documentos"
                        },
                        color = TextoSecundarioDocumentos,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 8.dp)
                    ) {
                        items(documentosFiltrados) { documento ->

                            val nombre = documento["nombre"] as? String
                                ?: "Sin nombre"

                            val tipo = documento["tipo"] as? String
                                ?: "Sin tipo"

                            val estado = documento["estado"] as? String
                                ?: "Sin estado"

                            val fecha = documento["fechaCarga"] as? Timestamp

                            val fechaTexto = if (fecha != null) {
                                SimpleDateFormat(
                                    "dd/MM/yyyy",
                                    Locale.getDefault()
                                ).format(fecha.toDate())
                            } else {
                                "Sin fecha"
                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        documentoSeleccionado = documento
                                    },
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
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(48.dp)
                                                .background(
                                                    AzulClaroDocumentos,
                                                    RoundedCornerShape(12.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Description,
                                                contentDescription = null,
                                                tint = AzulDocumentos,
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
                                                color = TextoDocumentos,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )

                                            Text(
                                                text = "$tipo · $fechaTexto",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = TextoSecundarioDocumentos
                                            )
                                        }

                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                            contentDescription = null,
                                            tint = AzulDocumentos
                                        )
                                    }

                                    EstadoDocumento(estado)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Volver al menú principal
        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = AzulDocumentos
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

// Se reutiliza también en DetalleDocumentoScreen
@Composable
fun EstadoDocumento(estado: String) {

    val colorFondo = when (estado) {
        "Aprobado" -> Color(0xFFDDF5E3)
        "Rechazado" -> Color(0xFFFFE0E0)
        "Pendiente" -> Color(0xFFFFF0C2)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    val colorTexto = when (estado) {
        "Aprobado" -> Color(0xFF176B35)
        "Rechazado" -> Color(0xFFB3261E)
        "Pendiente" -> Color(0xFF805500)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        color = colorFondo,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = estado,
            color = colorTexto,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 6.dp
            )
        )
    }
}
