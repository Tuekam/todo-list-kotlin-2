package com.example.domain.usecases

import com.example.core.interfaces.DeleteTaskRepository
import com.example.core.interfaces.DeleteTaskUseCase

class DeleteTaskUseCaseImpl(
    private val repository: DeleteTaskRepository
) : DeleteTaskUseCase {
    override suspend fun execute(id: String) {
        repository.delete(id)
    }
}
