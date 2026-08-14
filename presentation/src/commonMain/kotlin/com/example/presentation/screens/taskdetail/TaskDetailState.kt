package com.example.presentation.screens.taskdetail

import com.example.core.models.Task

data class TaskDetailState(
    val task: Task? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
