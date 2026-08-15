package com.example.app.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.example.presentation.screens.tasklist.TaskListScreen
import com.example.presentation.screens.taskdetail.TaskDetailScreen
import com.example.presentation.theme.AppTheme

@Composable
fun RootContent(component: RootComponent) {
    AppTheme {
        Children(
            stack = component.stack,
            animation = stackAnimation()
        ) {
            when (val child = it.instance) {
                is RootComponent.Child.ListChild -> TaskListScreen(
                    viewModel = child.viewModel,
                    navigation = component.taskListNavigation
                )
                is RootComponent.Child.DetailChild -> TaskDetailScreen(
                    viewModel = child.viewModel,
                    navigation = component.taskDetailNavigation
                )
            }
        }
    }
}
