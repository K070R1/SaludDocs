package com.saluddocs.app.ui.screens

import com.saluddocs.app.ui.components.SaludDocsTextField
import com.saluddocs.app.ui.components.SaludDocsButton
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
// contenido de  la pantalla
fun LoginScreen() {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SaludDocs",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Gestión documental de funcionarios")

        Spacer(modifier = Modifier.height(32.dp))

        SaludDocsTextField(
            valor = correo,
            alCambiar = { correo = it },
            etiqueta = "Correo electrónico"
        )

        Spacer(modifier = Modifier.height(16.dp))

        SaludDocsTextField(
            valor = contrasena,
            alCambiar = { contrasena = it },
            etiqueta = "Contraseña",
            esContrasena = true
        )

        Spacer(modifier = Modifier.height(24.dp))


        SaludDocsButton(
            texto = "Iniciar sesión",
            onClick = {
                mensaje = if (
                    correo.isNotBlank() && contrasena.isNotBlank()
                ) {
                    "Datos ingresados correctamente"
                } else {
                    "Completa todos los campos"
                }
            }
        )


        if (mensaje.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(mensaje)
        }
    }
}

