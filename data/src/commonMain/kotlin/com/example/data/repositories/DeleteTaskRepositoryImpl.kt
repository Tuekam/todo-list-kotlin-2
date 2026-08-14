package com.example.data.repositories

import com.example.core.interfaces.DeleteTaskRepository
import com.example.data.api.ApiService
import io.ktor.client.HttpClient
import io.ktor.client.request.delete

class DeleteTaskRepositoryImpl(
    private val client: HttpClient
) : DeleteTaskRepository {
    override suspend fun delete(id: String) {
        client.delete("${ApiService.BASE_URL}/api/tasks/$id")
    }
}
