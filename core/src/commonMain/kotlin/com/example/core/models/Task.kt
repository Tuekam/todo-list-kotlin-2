package com.example.core.models

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: String,
    val title: String,
    val completed: Boolean = false,
    val createdAt: String? = null // Le backend envoie une date ISO sous forme de String
)

@Serializable
data class TaskResult(
    val items: List<Task>,
    val nextCursor: String? = null,
    val hasMore: Boolean = false
)
