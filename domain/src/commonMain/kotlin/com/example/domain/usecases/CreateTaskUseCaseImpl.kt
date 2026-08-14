package com.example.domain.usecases

import com.example.core.interfaces.CreateTaskRepository
import com.example.core.interfaces.CreateTaskUseCase
import com.example.core.models.Task

class CreateTaskUseCaseImpl(
    private val repository: CreateTaskRepository
) : CreateTaskUseCase {
    override suspend fun execute(title: String): Task {
        require(title.isNotBlank()) { "Le titre ne peut pas être vide" }
        return repository.create(title.trim())
    }
}
