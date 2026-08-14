package com.example.core.models

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: String,
    val title: String,
    val completed: Boolean = false,
    val createdAt: String? = null
)

@Serializable
data class TaskResult(
    val items: List<Task>,
    val nextCursor: String? = null,
    val hasMore: Boolean = false
)

@Serializable
data class TaskFilters(
    val completed: Boolean? = null,
    val search: String? = null,
    val sort: String? = null,
    val direction: String = "asc",
    val limit: Int? = null,
    val lastDocId: String? = null
)
