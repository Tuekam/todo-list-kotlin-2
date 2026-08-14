package com.example.data.repositories

import com.example.core.interfaces.GetTasksRepository
import com.example.core.models.Task
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
    override suspend fun getAll(filters: TaskFilters?): TaskResult {
        return client.get("${ApiService.BASE_URL}/api/tasks") {
            filters?.let {
                parameter("completed", it.completed)
                parameter("search", it.search)
                parameter("sort", it.sort)
                parameter("direction", it.direction)
                parameter("limit", it.limit)
                parameter("lastDocId", it.lastDocId)
            }
        }.body()
    }

    override suspend fun getById(id: String): Task? {
        return try {
            client.get("${ApiService.BASE_URL}/api/tasks/$id").body()
        } catch (e: Exception) {
            null
        }
    }
}
