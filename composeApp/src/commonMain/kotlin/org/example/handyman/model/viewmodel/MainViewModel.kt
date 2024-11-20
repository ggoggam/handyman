package org.example.handyman.model.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PowerSettingsNew
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.example.handyman.model.channel.SnackbarMessageChannel
import org.example.handyman.model.data.Message
import org.example.handyman.model.data.Tool
import org.example.handyman.model.nav.NavigationRoute

class MainViewModel : ViewModel() {
    val query = MutableStateFlow("")

    var darkMode: Boolean? by mutableStateOf(null)
        private set

    val allTools = listOf(
        Tool(
            description = "Compute hash of a string",
            path = NavigationRoute.Hash,
            icon = Icons.Default.Tag
        ),
        Tool(
            description = "Get UUID from a string or randomly generate one",
            path = NavigationRoute.Uuid,
            icon = Icons.Outlined.Fingerprint
        ),
        Tool(
            description = "Convert timestamp to date time, vice versa",
            path = NavigationRoute.Epoch,
            icon = Icons.Outlined.Schedule
        ),
        Tool(
            description = "Encode/decode to/from base64 string",
            path = NavigationRoute.Base64,
            icon = Icons.Default.Pin
        ),
        Tool(
            description = "Encode/decode image to/from base64 string",
            path = NavigationRoute.Base64Image,
            icon = Icons.Outlined.Image
        ),
        Tool(
            description = "Translate binary to text, vice versa",
            path = NavigationRoute.Binary,
            icon = Icons.Outlined.PowerSettingsNew
        )
    )

    @OptIn(FlowPreview::class)
    val tools = query.asStateFlow()
        .debounce(150)
        .map { it.lowercase().trim() }
        .distinctUntilChanged()
        .map {
            if (it.isEmpty()) {
                allTools
            } else {
                allTools.filter { tool ->
                    tool.path.title.lowercase().contains(it) ||
                            tool.description.lowercase().contains(it)
                }
            }
        }
        .flowOn(Dispatchers.Main)

    var message: Message? by mutableStateOf(null)
        private set

    init {
        viewModelScope.launch {
            SnackbarMessageChannel.channel
                .collectLatest {
                    if (message?.message != it.message) {
                        message = it
                    }
                }
        }
    }

    fun resetMessage() {
        message = null
    }

    fun toggleDarkMode(darkMode: Boolean) {
        this.darkMode = darkMode
    }

    fun onQueryChange(query: String) {
        this.query.value = query
    }
}