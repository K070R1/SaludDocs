package com.saluddocs.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.saluddocs.app.repository.DocumentoRepository
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DocumentosScreen(
    onVolver: () -> Unit
) {
    val repository = remember { DocumentoRepository() }

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

        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            label = { Text("Buscar documento") },
            placeholder = { Text("Nombre, tipo o estado") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (cargando) {
            CircularProgressIndicator()
        } else if (mensaje.isNotEmpty()) {
            Text(mensaje)
        } else if (documentosFiltrados.isEmpty()) {
            Text(
                if (busqueda.isBlank()) {
                    "Todavía no tienes documentos registrados"
                } else {
                    "No se encontraron documentos"
                }
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
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
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = nombre,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text("Tipo: $tipo")
                            Text("Fecha: $fechaTexto")
                            Text("Estado: $estado")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al menú")
        }
    }
}
