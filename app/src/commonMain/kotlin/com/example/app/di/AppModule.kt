package com.example.app.di

import com.example.app.root.RootComponent
import com.example.app.root.RootComponentImpl
import com.example.data.di.dataModule
import com.example.domain.di.domainModule
import com.example.presentation.di.presentationModule
import com.arkivanov.decompose.ComponentContext
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
    single<RootComponent> { (context: ComponentContext) -> RootComponentImpl(context) }
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(dataModule, domainModule, presentationModule, appModule)
    }
}
