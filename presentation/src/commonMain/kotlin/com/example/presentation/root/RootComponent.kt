package com.example.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import com.example.presentation.screens.list.TaskListComponent
import kotlinx.serialization.builtins.serializer

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class ListChild(val component: TaskListComponent) : Child()
    }

    companion object
}

@Serializable
internal sealed class RootConfig {
    @Serializable
    data object List : RootConfig()
}

class RootComponentImpl(
    componentContext: ComponentContext,
    private val taskListFactory: (ComponentContext) -> TaskListComponent
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<RootConfig>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = RootConfig.serializer(),
            initialConfiguration = RootConfig.List,
            handleBackButton = true,
            childFactory = ::createChild
        )

    private fun createChild(config: RootConfig, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is RootConfig.List -> RootComponent.Child.ListChild(taskListFactory(componentContext))
        }
}
