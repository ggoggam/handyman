package org.example.handyman.model.data

import androidx.compose.ui.graphics.vector.ImageVector
import org.example.handyman.model.nav.NavigationRoute

data class Tool(
    val description: String,
    val path: NavigationRoute,
    val icon: ImageVector
)
