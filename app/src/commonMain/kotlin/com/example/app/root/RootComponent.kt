package com.example.app.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.example.presentation.screens.taskdetail.TaskDetailNavigation
import com.example.presentation.screens.tasklist.TaskListNavigation
import com.example.presentation.screens.tasklist.TaskListViewModel
import com.example.presentation.screens.taskdetail.TaskDetailViewModel
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class ListChild(val viewModel: TaskListViewModel) : Child()
        class DetailChild(val viewModel: TaskDetailViewModel) : Child()
    }
}

class RootComponentImpl(
    componentContext: ComponentContext
) : RootComponent, KoinComponent, ComponentContext by componentContext, TaskListNavigation, TaskDetailNavigation {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.List,
            handleBackButton = true,
            childFactory = ::createChild
        )

    private fun createChild(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.List -> RootComponent.Child.ListChild(
                get { parametersOf(this, componentContext) }
            )
            is Config.Detail -> RootComponent.Child.DetailChild(
                get { parametersOf(config.taskId, this, componentContext) }
            )
        }

    // TaskListNavigation Implementation
    override fun goToDetail(taskId: String) {
        navigation.push(Config.Detail(taskId))
    }

    // TaskDetailNavigation Implementation
    override fun goBack() {
        navigation.pop()
    }

    @Serializable
    private sealed class Config {
        @Serializable
        data object List : Config()
        @Serializable
        data class Detail(val taskId: String) : Config()
    }
}
