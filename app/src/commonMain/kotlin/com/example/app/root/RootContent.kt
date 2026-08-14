package com.example.app.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.example.presentation.screens.tasklist.TaskListScreen
import com.example.presentation.screens.taskdetail.TaskDetailScreen

@Composable
fun RootContent(component: RootComponent) {
    Children(
        stack = component.stack,
        animation = stackAnimation()
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.ListChild -> TaskListScreen(child.viewModel)
            is RootComponent.Child.DetailChild -> TaskDetailScreen(child.viewModel)
        }
    }
}
