package com.example.presentation.screens.tasklist

import com.example.core.interfaces.GetTasksUseCase
import com.example.core.interfaces.UpdateTaskUseCase
import com.example.core.interfaces.CreateTaskUseCase
import com.example.core.interfaces.DeleteTaskUseCase
import com.example.core.models.Task
import com.example.core.models.TaskFilters
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TaskListViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) {
    private val _state = MutableStateFlow(TaskListState())
    val state: StateFlow<TaskListState> = _state.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var refreshJob: Job? = null
    private var loadMoreJob: Job? = null

    init {
        refresh()
    }

    private fun showSuccess(message: String) {
        scope.launch {
            _state.update { it.copy(successMessage = message) }
            delay(2000)
            _state.update { it.copy(successMessage = "") }
        }
    }

    fun refresh() {
        refreshJob?.cancel()
        loadMoreJob?.cancel()
        _state.update { it.copy(isLoading = true, tasks = emptyList(), nextCursor = "", hasMore = false, error = "") }
        refreshJob = scope.launch {
            try {
                val currentState = _state.value
                val filters = TaskFilters(
                    completed = currentState.completedFilter,
                    search = currentState.searchQuery,
                    sort = currentState.sortBy,
                    direction = currentState.sortDirection,
                    limit = currentState.limit
                )
                
                val result = getTasksUseCase.execute(filters)
                _state.update {
                    it.copy(
                        tasks = result.items,
                        hasMore = result.hasMore,
                        nextCursor = result.nextCursor,
                        isLoading = false,
                        error = ""
                    )
                }
            } catch (e: Exception) {
                if (e !is CancellationException) {
                    _state.update { it.copy(error = e.message ?: "Erreur inconnue", isLoading = false) }
                }
            }
        }
    }

    fun loadMore() {
        val currentState = _state.value
        if (currentState.isLoading || currentState.isLoadingMore || !currentState.hasMore || currentState.nextCursor.isEmpty()) return

        loadMoreJob?.cancel()
        _state.update { it.copy(isLoadingMore = true) }
        loadMoreJob = scope.launch {
            try {
                val filters = TaskFilters(
                    completed = currentState.completedFilter,
                    search = currentState.searchQuery,
                    sort = currentState.sortBy,
                    direction = currentState.sortDirection,
                    limit = currentState.limit,
                    lastDocId = currentState.nextCursor
                )
                
                val result = getTasksUseCase.execute(filters)
                _state.update {
                    it.copy(
                        tasks = it.tasks + result.items,
                        hasMore = result.hasMore,
                        nextCursor = result.nextCursor,
                        isLoadingMore = false,
                        error = ""
                    )
                }
            } catch (e: Exception) {
                if (e !is CancellationException) {
                    _state.update { it.copy(error = e.message ?: "Erreur inconnue", isLoadingMore = false) }
                }
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
                showSuccess("Tâche ajoutée avec succès")
                refresh()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Erreur inconnue") }
            }
        }
    }

    fun onToggleTask(task: Task) {
        scope.launch {
            try {
                updateTaskUseCase.execute(id = task.id, completed = !task.completed)
                showSuccess(if (!task.completed) "Tâche terminée" else "Tâche réouverte")
                refresh()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Erreur inconnue") }
            }
        }
    }

    fun onStartEdit(task: Task) {
        _state.update { it.copy(editingTaskId = task.id, editingTitle = task.title) }
    }

    fun onCancelEdit() {
        _state.update { it.copy(editingTaskId = "", editingTitle = "") }
    }

    fun onEditingTitleChange(newTitle: String) {
        _state.update { it.copy(editingTitle = newTitle) }
    }

    fun onSaveEdit() {
        val taskId = _state.value.editingTaskId
        if (taskId.isEmpty()) return
        val newTitle = _state.value.editingTitle
        if (newTitle.isBlank()) return

        scope.launch {
            try {
                updateTaskUseCase.execute(id = taskId, title = newTitle)
                _state.update { it.copy(editingTaskId = "", editingTitle = "") }
                showSuccess("Tâche modifiée")
                refresh()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Erreur inconnue") }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
        refresh()
    }

    fun onFilterChange(completed: Boolean?) {
        _state.update { it.copy(completedFilter = completed) }
        refresh()
    }

    fun onSortChange(sortBy: String) {
        _state.update { 
            val newDirection = if (it.sortBy == sortBy) {
                if (it.sortDirection == "asc") "desc" else "asc"
            } else {
                "asc"
            }
            it.copy(sortBy = sortBy, sortDirection = newDirection) 
        }
        refresh()
    }

    fun onLimitChange(limit: Int?) {
        _state.update { it.copy(limit = limit ?: 10) }
        refresh()
    }

    fun requestDelete(task: Task) {
        _state.update { it.copy(taskToDelete = task) }
    }

    fun cancelDelete() {
        _state.update { it.copy(taskToDelete = null) }
    }

    fun confirmDelete() {
        val task = _state.value.taskToDelete ?: return
        _state.update { it.copy(taskToDelete = null) }
        
        scope.launch {
            try {
                deleteTaskUseCase.execute(task.id)
                showSuccess("Tâche supprimée")
                refresh()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Erreur inconnue") }
            }
        }
    }
}
