package com.example.presentation.screens.taskdetail

import com.example.core.interfaces.UpdateTaskUseCase
import com.example.core.models.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaskDetailViewModel(
    initialTask: Task,
    private val updateTaskUseCase: UpdateTaskUseCase
) {
    private val _state = MutableStateFlow(TaskDetailState(task = initialTask))
    val state: StateFlow<TaskDetailState> = _state.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun onToggleComplete() {
        val currentTask = _state.value.task ?: return
        scope.launch {
            try {
                val updated = updateTaskUseCase.execute(id = currentTask.id, completed = !currentTask.completed)
                _state.update { it.copy(task = updated) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }
}
