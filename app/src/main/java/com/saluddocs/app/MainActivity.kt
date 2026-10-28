package com.saluddocs.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.saluddocs.app.ui.screens.LoginScreen
import com.saluddocs.app.ui.theme.SaludDocsTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SaludDocsTheme {
                LoginScreen()
            }
        }
    }
}
