package com.example.core.interfaces

import com.example.core.models.Task
import com.example.core.models.TaskResult

interface GetTasksUseCase {
    suspend fun execute(): TaskResult
}

interface CreateTaskUseCase {
    suspend fun execute(title: String): Task
}

interface UpdateTaskUseCase {
    suspend fun execute(id: String, title: String? = null, completed: Boolean? = null): Task
}

interface DeleteTaskUseCase {
    suspend fun execute(id: String)
}
