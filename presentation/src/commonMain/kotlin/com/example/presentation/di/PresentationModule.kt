package com.example.presentation.di

import com.arkivanov.decompose.ComponentContext
import com.example.presentation.screens.tasklist.TaskListNavigation
import com.example.presentation.screens.tasklist.TaskListViewModel
import com.example.presentation.screens.taskdetail.TaskDetailNavigation
import com.example.presentation.screens.taskdetail.TaskDetailViewModel
import org.koin.dsl.module

val presentationModule = module {
    factory { (nav: TaskListNavigation, context: ComponentContext) ->
        TaskListViewModel(nav, get(), get(), get())
    }
    
    factory { (taskId: String, nav: TaskDetailNavigation, context: ComponentContext) ->
        TaskDetailViewModel(taskId, nav, get(), get(), get())
    }
}
