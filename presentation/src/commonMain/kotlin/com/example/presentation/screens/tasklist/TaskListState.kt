package com.example.presentation.screens.tasklist

import com.example.core.models.Task

data class TaskListState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasMore: Boolean = false,
    val nextCursor: String? = null,
    val newTaskTitle: String = ""
)
