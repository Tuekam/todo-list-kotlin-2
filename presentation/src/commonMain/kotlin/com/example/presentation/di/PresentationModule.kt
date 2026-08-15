package com.example.presentation.di

import com.arkivanov.decompose.ComponentContext
import com.example.core.models.Task
import com.example.presentation.screens.tasklist.TaskListViewModel
import com.example.presentation.screens.taskdetail.TaskDetailViewModel
import org.koin.dsl.module

val presentationModule = module {
    factory { 
        TaskListViewModel(get(), get(), get(), get())
    }
    
    factory { (initialTask: Task) ->
        TaskDetailViewModel(initialTask, get())
    }
}
