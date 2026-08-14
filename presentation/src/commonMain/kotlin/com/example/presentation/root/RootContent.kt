package com.example.presentation.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.example.presentation.screens.list.TaskListContent

@Composable
fun RootContent(component: RootComponent) {
    Children(
        stack = component.stack,
        animation = stackAnimation()
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.ListChild -> TaskListContent(child.component)
        }
    }
}
