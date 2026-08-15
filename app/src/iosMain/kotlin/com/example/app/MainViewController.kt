package com.example.app

import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.example.app.di.initKoin
import com.example.app.root.RootComponent
import com.example.app.root.RootContent
import org.koin.core.parameter.parametersOf
import org.koin.mp.KoinPlatform
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    // Initialisation Koin pour iOS
    try {
        initKoin()
    } catch (_: Exception) {
        // Déjà initialisé
    }

    val lifecycle = LifecycleRegistry()
    val root = KoinPlatform.getKoin().get<RootComponent> {
        parametersOf(DefaultComponentContext(lifecycle))
    }

    return ComposeUIViewController {
        RootContent(root)
    }
}
