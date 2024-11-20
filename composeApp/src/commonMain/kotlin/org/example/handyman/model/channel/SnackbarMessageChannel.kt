package org.example.handyman.model.channel

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import org.example.handyman.model.data.Message

object SnackbarMessageChannel {
    private val _channel: Channel<Message> = Channel(capacity = 16)
    val channel = _channel.receiveAsFlow()

    suspend fun emit(message: Message) {
        _channel.send(message)
    }
}

val LocalSnackbarMessageChannel = staticCompositionLocalOf<SnackbarMessageChannel> {
    SnackbarMessageChannel
}