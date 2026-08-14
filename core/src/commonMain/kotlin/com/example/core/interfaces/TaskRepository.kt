package com.example.core.interfaces

import com.example.core.models.Task
import com.example.core.models.TaskResult
import com.example.core.models.TaskFilters

interface GetTasksRepository {
    suspend fun getAll(filters: TaskFilters? = null): TaskResult
    suspend fun getById(id: String): Task?
}

interface CreateTaskRepository {
    suspend fun create(title: String): Task
}

interface UpdateTaskRepository {
    suspend fun update(id: String, title: String? = null, completed: Boolean? = null): Task
}

interface DeleteTaskRepository {
    suspend fun delete(id: String)
}
