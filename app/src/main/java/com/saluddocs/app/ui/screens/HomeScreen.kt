package com.saluddocs.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saluddocs.app.repository.UsuarioRepository
import kotlinx.coroutines.delay

private val AzulSalud = Color(0xFF1769AA)
private val AzulClaroSalud = Color(0xFFE4F1FF)
private val FondoSalud = Color(0xFFF5F8FC)

@Composable
fun HomeScreen(
    onCerrarSesion: () -> Unit,
    onMisDocumentos: () -> Unit,
    onSubirDocumento: () -> Unit,
    onRevisarDocumentos: () -> Unit,
    onDocumentosLocales: () -> Unit
) {
    val repository = remember { UsuarioRepository() }

    var rol by remember { mutableStateOf<String?>(null) }
    var cargando by remember { mutableStateOf(true) }
    var mensaje by remember { mutableStateOf("") }

    var mostrarPrimera by remember { mutableStateOf(false) }
    var mostrarSegunda by remember { mutableStateOf(false) }
    var mostrarTercera by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        repository.obtenerRol { rolObtenido, error ->
            rol = rolObtenido
            mensaje = error
            cargando = false
        }
    }

    // Animación escalonada al entrar al menú
    LaunchedEffect(cargando, rol, mensaje) {
        if (!cargando && mensaje.isEmpty() && rol != null) {
            mostrarPrimera = false
            mostrarSegunda = false
            mostrarTercera = false

            delay(300)
            mostrarPrimera = true

            delay(500)
            mostrarSegunda = true

            delay(500)
            mostrarTercera = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoSalud)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = AzulSalud
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )

                    Text(
                        text = "SaludDocs",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Text(
                    text = "Gestión documental de salud",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFD9EDFF)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "¡Bienvenido!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Gestiona tus documentos de forma fácil y segura.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )

                if (!cargando && mensaje.isEmpty() && rol != null) {
                    Surface(
                        color = Color.White.copy(alpha = 0.18f),
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Text(
                            text = if (rol == "administrador") {
                                "Administrador"
                            } else {
                                "Funcionario"
                            },
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(
                                horizontal = 14.dp,
                                vertical = 7.dp
                            )
                        )
                    }
                }
            }
        }

        if (cargando) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AzulSalud)
            }
        } else if (mensaje.isNotEmpty()) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFE9E9)
                )
            ) {
                Text(
                    text = mensaje,
                    color = Color(0xFFB3261E),
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            Text(
                text = "¿Qué deseas hacer?",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF203248)
            )

            AnimatedVisibility(
                visible = mostrarPrimera,
                enter = fadeIn(tween(900)) +
                        slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(900)
                        )
            ) {
                OpcionSaludDocs(
                    titulo = "Mis documentos",
                    descripcion = "Consulta y revisa tus documentos",
                    icono = Icons.Default.Description,
                    onClick = onMisDocumentos
                )
            }

            AnimatedVisibility(
                visible = mostrarSegunda,
                enter = fadeIn(tween(900)) +
                        slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(900)
                        )
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (rol == "funcionario") {
                        OpcionSaludDocs(
                            titulo = "Subir documento",
                            descripcion = "Registra un nuevo documento PDF",
                            icono = Icons.Default.AddCircle,
                            onClick = onSubirDocumento
                        )
                    }

                    if (rol == "administrador") {
                        OpcionSaludDocs(
                            titulo = "Revisar documentos",
                            descripcion = "Aprueba o rechaza documentos pendientes",
                            icono = Icons.Default.CheckCircle,
                            onClick = onRevisarDocumentos
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = mostrarTercera,
                enter = fadeIn(tween(900)) +
                        slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(900)
                        )
            ) {
                OpcionSaludDocs(
                    titulo = "Documentos locales",
                    descripcion = "Registra, consulta, edita y elimina documentos en el dispositivo",
                    icono = Icons.Default.Folder,
                    onClick = onDocumentosLocales
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onCerrarSesion,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = AzulSalud
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text("Cerrar sesión")
        }
    }
}

@Composable
private fun OpcionSaludDocs(
    titulo: String,
    descripcion: String,
    icono: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        AzulClaroSalud,
                        RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = AzulSalud,
                    modifier = Modifier.size(28.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF203248)
                )

                Text(
                    text = descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF64748B)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = AzulSalud
            )
        }
    }
}
