package com.example.presentation.screens.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.example.core.interfaces.GetTasksUseCase
import com.example.core.interfaces.ToggleTaskUseCase
import com.example.core.models.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

interface TaskListComponent {
    val state: Value<State>
    fun onToggleTask(task: Task)
    fun refresh()

    data class State(
        val tasks: List<Task> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val hasMore: Boolean = false,
        val nextCursor: String? = null
    )
}

class TaskListComponentImpl(
    componentContext: ComponentContext,
    private val getTasksUseCase: GetTasksUseCase,
    private val toggleTaskUseCase: ToggleTaskUseCase
) : TaskListComponent, ComponentContext by componentContext {

    private val _state = MutableValue(TaskListComponent.State())
    override val state: Value<TaskListComponent.State> = _state

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    init {
        refresh()
    }

    override fun onToggleTask(task: Task) {
        scope.launch {
            try {
                toggleTaskUseCase.execute(task.id, !task.completed)
                refresh()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    override fun refresh() {
        _state.value = _state.value.copy(isLoading = true)
        scope.launch {
            try {
                val result = getTasksUseCase.execute()
                _state.value = _state.value.copy(
                    tasks = result.items,
                    hasMore = result.hasMore,
                    nextCursor = result.nextCursor,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isLoading = false)
            }
        }
    }
}
