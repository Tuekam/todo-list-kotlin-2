package com.example.domain.usecases

import com.example.core.interfaces.UpdateTaskRepository
import com.example.core.interfaces.UpdateTaskUseCase
import com.example.core.models.Task

class UpdateTaskUseCaseImpl(
    private val repository: UpdateTaskRepository
) : UpdateTaskUseCase {
    override suspend fun execute(id: String, title: String?, completed: Boolean?): Task {
        return repository.update(id = id, title = title, completed = completed)
    }
}
