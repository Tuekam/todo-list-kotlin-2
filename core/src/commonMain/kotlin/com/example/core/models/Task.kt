package com.example.core.models

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: String,
    val title: String,
    val completed: Boolean = false,
    val createdAt: String = ""
)

@Serializable
data class TaskResult(
    val items: List<Task> = emptyList(),
    val nextCursor: String = "",
    val hasMore: Boolean = false
)

@Serializable
data class TaskFilters(
    val completed: Boolean? = null,
    val search: String = "",
    val sort: String = "createdAt",
    val direction: String = "desc",
    val limit: Int = 10,
    val lastDocId: String = ""
)
