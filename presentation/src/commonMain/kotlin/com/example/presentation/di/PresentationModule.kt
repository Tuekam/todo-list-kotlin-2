package com.example.presentation.di

import com.arkivanov.decompose.ComponentContext
import com.example.presentation.root.RootComponent
import com.example.presentation.root.RootComponentImpl
import com.example.presentation.screens.list.TaskListComponent
import com.example.presentation.screens.list.TaskListComponentImpl
import org.koin.dsl.module

val presentationModule = module {
    factory<TaskListComponent> { (context: ComponentContext) -> 
        TaskListComponentImpl(context, get(), get()) 
    }
    
    factory<RootComponent> { (context: ComponentContext) ->
        RootComponentImpl(context) { childContext ->
            get { org.koin.core.parameter.parametersOf(childContext) }
        }
    }
}
