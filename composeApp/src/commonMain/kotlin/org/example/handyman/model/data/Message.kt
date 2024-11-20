package org.example.handyman.model.data

import java.util.UUID

data class Message(
    val id: UUID =  UUID.randomUUID(),
    val message: String,
    val action: () -> Unit = {}
)