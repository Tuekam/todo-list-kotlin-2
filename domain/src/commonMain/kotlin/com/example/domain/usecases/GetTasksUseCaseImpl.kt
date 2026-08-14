package com.example.domain.usecases

import com.example.core.interfaces.GetTasksRepository
import com.example.core.interfaces.GetTasksUseCase
import com.example.core.models.TaskResult

class GetTasksUseCaseImpl(
    private val repository: GetTasksRepository
) : GetTasksUseCase {
    override suspend fun execute(): TaskResult = repository.getAll()
}
