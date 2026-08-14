package com.example.data.repositories

import com.example.core.interfaces.CreateTaskRepository
import com.example.core.models.Task
import com.example.data.api.ApiService
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

@Serializable
private data class CreateTaskRequest(val title: String)

class CreateTaskRepositoryImpl(
    private val client: HttpClient
) : CreateTaskRepository {
    override suspend fun create(title: String): Task {
        return client.post("${ApiService.BASE_URL}/api/tasks") {
            contentType(ContentType.Application.Json)
            setBody(CreateTaskRequest(title))
        }.body()
    }
}
