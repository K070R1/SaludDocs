package com.saluddocs.app.ui.screens

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

    val fechaTexto = if (fecha != null) {
        SimpleDateFormat(
            "dd/MM/yyyy HH:mm",
            Locale.getDefault()
        ).format(fecha.toDate())
    } else {
        "Sin fecha"
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
                Text("Estado: $estado")
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
