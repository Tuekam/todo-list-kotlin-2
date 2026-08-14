package com.example.presentation.screens.tasklist

import com.example.core.models.Task

data class TaskListState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasMore: Boolean = false,
    val nextCursor: String? = null,
    
    // Formulaire intégré
    val newTaskTitle: String = "",
    
    // Filtres et Tri
    val searchQuery: String = "",
    val completedFilter: Boolean? = null,
    val sortBy: String? = "createdAt",
    val sortDirection: String = "desc",
    val limit: Int? = 10,
    
    // Gestion de la suppression
    val taskToDelete: Task? = null
)
