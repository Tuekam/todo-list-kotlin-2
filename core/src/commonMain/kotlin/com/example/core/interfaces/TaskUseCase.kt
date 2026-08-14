package com.example.core.interfaces

import com.example.core.models.Task
import com.example.core.models.TaskResult

interface GetTasksUseCase {
    suspend fun execute(): TaskResult
}

interface CreateTaskUseCase {
    suspend fun execute(title: String): Task
}

interface ToggleTaskUseCase {
    suspend fun execute(id: String, completed: Boolean): Task
}

interface DeleteTaskUseCase {
    suspend fun execute(id: String)
}
