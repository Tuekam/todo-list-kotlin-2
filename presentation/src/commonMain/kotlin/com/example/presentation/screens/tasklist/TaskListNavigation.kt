package com.example.presentation.screens.tasklist

import com.example.core.models.Task

interface TaskListNavigation {
    fun goToDetail(task: Task)
}
