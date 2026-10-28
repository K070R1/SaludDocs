package com.saluddocs.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saluddocs.app.model.Documento

@Composable
fun DocumentosScreen(
    onVolver: () -> Unit
) {
    val documentos = listOf(
        Documento(1, "Contrato de trabajo", "Contrato", "20/10/2026", "Aprobado"),
        Documento(2, "Certificado laboral", "Certificado", "21/10/2026", "Pendiente"),
        Documento(3, "Anexo de contrato", "Anexo", "22/10/2026", "Rechazado")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Mis documentos",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(documentos) { documento ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = documento.nombre,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("Tipo: ${documento.tipo}")
                        Text("Fecha: ${documento.fechaCarga}")
                        Text("Estado: ${documento.estado}")
                    }
                }
            }
        }

        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al menú")
        }
    }
}
