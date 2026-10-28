package com.saluddocs.app.ui.screens

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.saluddocs.app.repository.UsuarioRepository
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale


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
            UsuarioRepository().obtenerNombreAdministrador(revisadoPor) { nombre, error ->
                nombreAdministrador = nombre
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
            .padding(24.dp)
    ) {
        Text(
            text = "Detalle del documento",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = nombre,
                    style = MaterialTheme.typography.titleLarge
                )

                HorizontalDivider()

                Text("Tipo: $tipo")
                Text("Fecha de registro: $fechaTexto")

                // Mostrar el estado con su color
                Text("Estado del documento:")

                EstadoDocumento(estado = estado)

                if (fechaRevisionTexto != null) {
                    Text("Fecha de revisión: $fechaRevisionTexto")
                }

                if (!revisadoPor.isNullOrBlank()) {
                    Text(
                        text = "Revisado por: ${nombreAdministrador ?: "Administrador"}"
                    )
                }

                HorizontalDivider()

                Text("Archivo PDF: todavía no disponible")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver a Mis documentos")
        }
    }
}
