package com.saluddocs.app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun SaludDocsTextField(
    valor: String,
    alCambiar: (String) -> Unit,
    etiqueta: String,
    esContrasena: Boolean = false,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = valor,
        onValueChange = alCambiar,
        label = { Text(etiqueta) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (esContrasena) {
                KeyboardType.Password
            } else {
                KeyboardType.Email
            }
        ),
        visualTransformation = if (esContrasena) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        }
    )
}
