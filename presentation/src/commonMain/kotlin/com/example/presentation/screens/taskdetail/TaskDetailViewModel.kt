package com.example.presentation.screens.taskdetail

import com.example.core.interfaces.GetTasksRepository
import com.example.core.interfaces.UpdateTaskUseCase
import com.example.core.interfaces.DeleteTaskUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaskDetailViewModel(
    private val taskId: String,
    private val navigation: TaskDetailNavigation,
    private val repository: GetTasksRepository,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) {
    private val _state = MutableStateFlow(TaskDetailState())
    val state: StateFlow<TaskDetailState> = _state.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    init {
        loadTask()
    }

    private fun loadTask() {
        _state.update { it.copy(isLoading = true) }
        scope.launch {
            try {
                val task = repository.getById(taskId)
                _state.update { it.copy(task = task, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun onToggleComplete() {
        val currentTask = _state.value.task ?: return
        scope.launch {
            try {
                val updated = updateTaskUseCase.execute(id = taskId, completed = !currentTask.completed)
                _state.update { it.copy(task = updated) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    fun onDelete() {
        scope.launch {
            try {
                deleteTaskUseCase.execute(taskId)
                navigation.goBack()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    fun onBackClick() {
        navigation.goBack()
    }
}
