package com.example.domain.usecases

import com.example.core.interfaces.GetTasksRepository
import com.example.core.interfaces.GetTasksUseCase
import com.example.core.models.TaskResult
import com.example.core.models.TaskFilters

class GetTasksUseCaseImpl(
    private val repository: GetTasksRepository
) : GetTasksUseCase {
    override suspend fun execute(filters: TaskFilters): TaskResult = repository.getAll(filters)
}
