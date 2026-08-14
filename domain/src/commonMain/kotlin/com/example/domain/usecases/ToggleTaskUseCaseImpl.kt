package com.example.domain.usecases

import com.example.core.interfaces.ToggleTaskUseCase
import com.example.core.interfaces.UpdateTaskRepository
import com.example.core.models.Task

class ToggleTaskUseCaseImpl(
    private val repository: UpdateTaskRepository
) : ToggleTaskUseCase {
    override suspend fun execute(id: String, completed: Boolean): Task {
        return repository.update(id = id, completed = completed)
    }
}
