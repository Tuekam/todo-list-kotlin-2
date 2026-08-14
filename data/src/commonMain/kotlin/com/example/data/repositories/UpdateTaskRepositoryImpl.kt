package com.example.data.repositories

import com.example.core.interfaces.UpdateTaskRepository
import com.example.core.models.Task
import com.example.data.api.ApiService
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

@Serializable
private data class UpdateTaskRequest(
    val title: String? = null,
    val completed: Boolean? = null
)

class UpdateTaskRepositoryImpl(
    private val client: HttpClient
) : UpdateTaskRepository {
    override suspend fun update(id: String, title: String?, completed: Boolean?): Task {
        return client.patch("${ApiService.BASE_URL}/api/tasks/$id") {
            contentType(ContentType.Application.Json)
            setBody(UpdateTaskRequest(title, completed))
        }.body()
    }
}
