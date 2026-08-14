package com.example.todo_list_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.example.app.di.initKoin
import com.example.presentation.root.RootComponent
import com.example.presentation.root.RootContent
import com.example.presentation.screens.list.TaskListComponent
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
        } catch (e: Exception) {
            // Déjà initialisé
        }

        // Utilisation de Koin pour récupérer le RootComponent de manière abstraite
        val root: RootComponent = KoinPlatform.getKoin().get<RootComponent> { 
            parametersOf(defaultComponentContext()) 
        }

        setContent {
            RootContent(root)
        }
    }
}
