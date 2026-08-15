package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.example.app.di.initKoin
import com.example.app.root.RootComponent
import com.example.app.root.RootContent
import org.koin.android.ext.koin.androidContext
import org.koin.core.parameter.parametersOf
import org.koin.mp.KoinPlatform

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialisation Koin avec le contexte Android
        try {
            initKoin {
                androidContext(this@MainActivity)
            }
        } catch (_: Exception) {
            // Déjà initialisé
        }

        // Récupération du RootComponent via Koin
        val root: RootComponent = KoinPlatform.getKoin().get { 
            parametersOf(defaultComponentContext()) 
        }

        setContent {
            RootContent(root)
        }
    }
}
