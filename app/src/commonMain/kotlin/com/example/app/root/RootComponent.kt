package com.example.app.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.example.core.models.Task
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
    
    val taskListNavigation: TaskListNavigation
    val taskDetailNavigation: TaskDetailNavigation

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

    override val taskListNavigation: TaskListNavigation = this
    override val taskDetailNavigation: TaskDetailNavigation = this

    private fun createChild(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.List -> RootComponent.Child.ListChild(
                get { parametersOf(componentContext) }
            )
            is Config.Detail -> RootComponent.Child.DetailChild(
                get { parametersOf(config.task) }
            )
        }

    @OptIn(DelicateDecomposeApi::class)
    override fun goToDetail(task: Task) {
        navigation.push(Config.Detail(task))
    }

    override fun goBack() {
        navigation.pop()
    }

    @Serializable
    private sealed class Config {
        @Serializable
        data object List : Config()
        @Serializable
        data class Detail(val task: Task) : Config()
    }
}
