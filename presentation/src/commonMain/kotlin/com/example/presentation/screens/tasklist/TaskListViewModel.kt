package com.example.presentation.screens.tasklist

import com.example.core.interfaces.GetTasksUseCase
import com.example.core.interfaces.UpdateTaskUseCase
import com.example.core.interfaces.CreateTaskUseCase
import com.example.core.models.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaskListViewModel(
    private val navigation: TaskListNavigation,
    private val getTasksUseCase: GetTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val createTaskUseCase: CreateTaskUseCase
) {
    private val _state = MutableStateFlow(TaskListState())
    val state: StateFlow<TaskListState> = _state.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    init {
        refresh()
    }

    fun refresh() {
        _state.update { it.copy(isLoading = true) }
        scope.launch {
            try {
                val result = getTasksUseCase.execute()
                _state.update {
                    it.copy(
                        tasks = result.items,
                        hasMore = result.hasMore,
                        nextCursor = result.nextCursor,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun onNewTaskTitleChange(title: String) {
        _state.update { it.copy(newTaskTitle = title) }
    }

    fun onCreateTask() {
        val title = _state.value.newTaskTitle
        if (title.isBlank()) return

        scope.launch {
            try {
                createTaskUseCase.execute(title)
                _state.update { it.copy(newTaskTitle = "") }
                refresh()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    fun onToggleTask(task: Task) {
        scope.launch {
            try {
                updateTaskUseCase.execute(id = task.id, completed = !task.completed)
                refresh()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    fun onTaskClick(task: Task) {
        navigation.goToDetail(task.id)
    }
}
