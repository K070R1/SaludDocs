package com.saluddocs.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.saluddocs.app.viewmodel.LoginViewModel

private val AzulLogin = Color(0xFF1769AA)
private val AzulOscuroLogin = Color(0xFF124A96)
private val CelesteLogin = Color(0xFF35B4EE)
private val FondoLogin = Color(0xFFF5F8FC)
private val TextoLogin = Color(0xFF203248)
private val TextoSecundarioLogin = Color(0xFF64748B)

@Composable
fun LoginScreen(
    onLoginExitoso: () -> Unit,
    loginViewModel: LoginViewModel = viewModel()
) {
    var mostrarContrasena by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoLogin)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // Encabezado azul con logo grande
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(265.dp)
            ) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    drawRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                AzulOscuroLogin,
                                AzulLogin,
                                CelesteLogin
                            )
                        )
                    )

                    val ancho = size.width
                    val alto = size.height

                    val ondaCeleste = Path().apply {
                        moveTo(0f, alto * 0.76f)
                        cubicTo(
                            ancho * 0.28f, alto * 0.55f,
                            ancho * 0.58f, alto * 1.06f,
                            ancho, alto * 0.79f
                        )
                        lineTo(ancho, alto)
                        lineTo(0f, alto)
                        close()
                    }

                    drawPath(
                        path = ondaCeleste,
                        color = CelesteLogin
                    )

                    val ondaBlanca = Path().apply {
                        moveTo(0f, alto * 0.81f)
                        cubicTo(
                            ancho * 0.28f, alto * 0.60f,
                            ancho * 0.58f, alto * 1.11f,
                            ancho, alto * 0.84f
                        )
                        lineTo(ancho, alto)
                        lineTo(0f, alto)
                        close()
                    }

                    drawPath(
                        path = ondaBlanca,
                        color = FondoLogin
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LogoCorazonSaludDocs()

                    Text(
                        text = "SaludDocs",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "Gestión documental de salud",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            // Tarjeta blanca del formulario
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 8.dp
                    ),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {

                    Text(
                        text = "Bienvenido",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = AzulOscuroLogin
                    )

                    Text(
                        text = "Inicia sesión con tu cuenta de SaludDocs",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoSecundarioLogin
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Correo electrónico
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "CORREO ELECTRÓNICO",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = AzulLogin
                        )

                        OutlinedTextField(
                            value = loginViewModel.correo,
                            onValueChange = {
                                loginViewModel.actualizarCorreo(it)
                            },
                            placeholder = {
                                Text("usuario@ejemplo.cl")
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Mail,
                                    contentDescription = null,
                                    tint = AzulLogin
                                )
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email
                            ),
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AzulLogin,
                                unfocusedBorderColor = Color(0xFFD7E2EE),
                                focusedContainerColor = FondoLogin,
                                unfocusedContainerColor = FondoLogin
                            )
                        )
                    }

                    // Contraseña
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "CONTRASEÑA",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = AzulLogin
                        )

                        OutlinedTextField(
                            value = loginViewModel.contrasena,
                            onValueChange = {
                                loginViewModel.actualizarContrasena(it)
                            },
                            placeholder = {
                                Text("Ingresa tu contraseña")
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = AzulLogin
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        mostrarContrasena = !mostrarContrasena
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (mostrarContrasena) {
                                            Icons.Default.VisibilityOff
                                        } else {
                                            Icons.Default.Visibility
                                        },
                                        contentDescription = if (mostrarContrasena) {
                                            "Ocultar contraseña"
                                        } else {
                                            "Mostrar contraseña"
                                        },
                                        tint = TextoSecundarioLogin
                                    )
                                }
                            },
                            visualTransformation = if (mostrarContrasena) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password
                            ),
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AzulLogin,
                                unfocusedBorderColor = Color(0xFFD7E2EE),
                                focusedContainerColor = FondoLogin,
                                unfocusedContainerColor = FondoLogin
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Iniciar sesión usando el ViewModel actual
                    Button(
                        onClick = {
                            loginViewModel.iniciarSesion(
                                onLoginExitoso = onLoginExitoso
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AzulLogin
                        )
                    ) {
                        Text(
                            text = "Ingresar",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null
                        )
                    }

                    // Mensajes del inicio de sesión
                    if (loginViewModel.mensaje.isNotEmpty()) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = FondoLogin,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = loginViewModel.mensaje,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextoLogin,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }

                    HorizontalDivider(
                        color = Color(0xFFE2E8F0)
                    )

                    Text(
                        text = "Acceso exclusivo para funcionarios y administradores",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundarioLogin,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Onda inferior
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(105.dp)
            ) {
                val ancho = size.width
                val alto = size.height

                val ondaCeleste = Path().apply {
                    moveTo(0f, alto * 0.30f)
                    cubicTo(
                        ancho * 0.30f, 0f,
                        ancho * 0.60f, alto * 0.95f,
                        ancho, alto * 0.38f
                    )
                    lineTo(ancho, alto)
                    lineTo(0f, alto)
                    close()
                }

                drawPath(
                    path = ondaCeleste,
                    color = CelesteLogin
                )

                val ondaAzul = Path().apply {
                    moveTo(0f, alto * 0.43f)
                    cubicTo(
                        ancho * 0.30f, alto * 0.13f,
                        ancho * 0.60f, alto * 1.08f,
                        ancho, alto * 0.51f
                    )
                    lineTo(ancho, alto)
                    lineTo(0f, alto)
                    close()
                }

                drawPath(
                    path = ondaAzul,
                    color = AzulLogin
                )
            }
        }
    }
}

// Logo de corazón con electrocardiograma
@Composable
private fun LogoCorazonSaludDocs() {
    Box(
        modifier = Modifier.size(
            width = 104.dp,
            height = 82.dp
        ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Logo de SaludDocs",
            tint = Color.White,
            modifier = Modifier.size(86.dp)
        )

        Canvas(
            modifier = Modifier.size(
                width = 67.dp,
                height = 35.dp
            )
        ) {
            val ancho = size.width
            val alto = size.height

            val linea = Path().apply {
                moveTo(0f, alto * 0.54f)
                lineTo(ancho * 0.25f, alto * 0.54f)
                lineTo(ancho * 0.36f, alto * 0.24f)
                lineTo(ancho * 0.47f, alto * 0.85f)
                lineTo(ancho * 0.58f, alto * 0.40f)
                lineTo(ancho * 0.70f, alto * 0.54f)
                lineTo(ancho, alto * 0.54f)
            }

            drawPath(
                path = linea,
                color = AzulLogin,
                style = Stroke(
                    width = 3.dp.toPx()
                )
            )
        }
    }
}
