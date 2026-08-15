package com.example.data.repositories

import com.example.core.interfaces.GetTasksRepository
import com.example.core.models.TaskResult
import com.example.core.models.TaskFilters
import com.example.data.api.ApiService
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class GetTasksRepositoryImpl(
    private val client: HttpClient
) : GetTasksRepository {
    override suspend fun getAll(filters: TaskFilters): TaskResult {
        return client.get("${ApiService.BASE_URL}/api/tasks") {
            parameter("completed", filters.completed)
            parameter("search", filters.search.takeIf { it.isNotBlank() })
            parameter("sort", filters.sort)
            parameter("direction", filters.direction)
            parameter("limit", filters.limit)
            parameter("lastDocId", filters.lastDocId.takeIf { it.isNotBlank() })
        }.body()
    }
}
