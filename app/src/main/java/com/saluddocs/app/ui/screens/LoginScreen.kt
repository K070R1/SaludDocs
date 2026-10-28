package com.saluddocs.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.saluddocs.app.ui.components.SaludDocsButton
import com.saluddocs.app.ui.components.SaludDocsTextField
import com.saluddocs.app.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onLoginExitoso: () -> Unit,
    loginViewModel: LoginViewModel = viewModel()
) {
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
            valor = loginViewModel.correo,
            alCambiar = {
                loginViewModel.actualizarCorreo(it)
            },
            etiqueta = "Correo electrónico"
        )

        Spacer(modifier = Modifier.height(16.dp))

        SaludDocsTextField(
            valor = loginViewModel.contrasena,
            alCambiar = {
                loginViewModel.actualizarContrasena(it)
            },
            etiqueta = "Contraseña",
            esContrasena = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        SaludDocsButton(
            texto = "Iniciar sesión",
            onClick = {
                loginViewModel.iniciarSesion()

                if (
                    loginViewModel.correo.isNotBlank() &&
                    loginViewModel.contrasena.isNotBlank()
                ) {
                    onLoginExitoso()
                }
            }
        )

        if (loginViewModel.mensaje.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(loginViewModel.mensaje)
        }
    }
}
