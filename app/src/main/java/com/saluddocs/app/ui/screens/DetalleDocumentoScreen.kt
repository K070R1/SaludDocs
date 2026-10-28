package com.saluddocs.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.saluddocs.app.repository.UsuarioRepository
import java.text.SimpleDateFormat
import java.util.Locale

private val AzulDetalle = Color(0xFF1769AA)
private val AzulClaroDetalle = Color(0xFFE4F1FF)
private val FondoDetalle = Color(0xFFF5F8FC)
private val TextoDetalle = Color(0xFF203248)
private val TextoSecundarioDetalle = Color(0xFF64748B)

@Composable
fun DetalleDocumentoScreen(
    documento: Map<String, Any>,
    onVolver: () -> Unit
) {
    val nombre = documento["nombre"] as? String ?: "Sin nombre"
    val tipo = documento["tipo"] as? String ?: "Sin tipo"
    val estado = documento["estado"] as? String ?: "Sin estado"
    val fecha = documento["fechaCarga"] as? Timestamp
    val fechaRevision = documento["fechaRevision"] as? Timestamp
    val revisadoPor = documento["revisadoPor"] as? String

    var nombreAdministrador by remember(revisadoPor) {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(revisadoPor) {
        if (!revisadoPor.isNullOrBlank()) {
            UsuarioRepository().obtenerNombreAdministrador(
                revisadoPor
            ) { nombreObtenido, _ ->
                nombreAdministrador = nombreObtenido
            }
        }
    }

    val fechaTexto = if (fecha != null) {
        SimpleDateFormat(
            "dd/MM/yyyy HH:mm",
            Locale.getDefault()
        ).format(fecha.toDate())
    } else {
        "Sin fecha"
    }

    val fechaRevisionTexto = fechaRevision?.let {
        SimpleDateFormat(
            "dd/MM/yyyy HH:mm",
            Locale.getDefault()
        ).format(it.toDate())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoDetalle)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Contenido desplazable
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            // Encabezado
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AzulDetalle
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
                            text = "Detalle del documento",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Text(
                        text = "Consulta la información y el estado de revisión.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFD9EDFF)
                    )
                }
            }

            // Tarjeta de información
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
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {

                    // Nombre e icono del documento
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(58.dp)
                                .background(
                                    AzulClaroDetalle,
                                    RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                tint = AzulDetalle,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = nombre,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextoDetalle,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = "Tipo: $tipo",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextoSecundarioDetalle
                            )
                        }
                    }

                    HorizontalDivider(
                        color = Color(0xFFE2E8F0)
                    )

                    // Fecha de registro
                    CampoDetalle(
                        titulo = "Fecha de registro",
                        valor = fechaTexto
                    )

                    // Estado
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Estado del documento",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextoSecundarioDetalle
                        )

                        EstadoDocumento(estado = estado)
                    }

                    // Historial de revisión
                    if (fechaRevisionTexto != null ||
                        !revisadoPor.isNullOrBlank()
                    ) {
                        HorizontalDivider(
                            color = Color(0xFFE2E8F0)
                        )

                        Text(
                            text = "Información de revisión",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextoDetalle
                        )

                        if (fechaRevisionTexto != null) {
                            CampoDetalle(
                                titulo = "Fecha de revisión",
                                valor = fechaRevisionTexto
                            )
                        }

                        if (!revisadoPor.isNullOrBlank()) {
                            CampoDetalle(
                                titulo = "Revisado por",
                                valor = nombreAdministrador
                                    ?: "Administrador"
                            )
                        }
                    }

                    // Aviso sobre el archivo PDF
                    Surface(
                        color = Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = TextoSecundarioDetalle
                            )

                            Text(
                                text = "Archivo PDF todavía no disponible",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextoSecundarioDetalle
                            )
                        }
                    }
                }
            }
        }

        // Botón de regreso
        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = AzulDetalle
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text("Volver a Mis documentos")
        }
    }
}

@Composable
private fun CampoDetalle(
    titulo: String,
    valor: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.labelMedium,
            color = TextoSecundarioDetalle
        )

        Text(
            text = valor,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = TextoDetalle
        )
    }
}
