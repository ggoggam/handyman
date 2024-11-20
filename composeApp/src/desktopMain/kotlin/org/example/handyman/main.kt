package org.example.handyman

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.Dimension

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "handyman",
        resizable = true,
    ) {
        window.minimumSize = Dimension(512, 800)
        App()
    }
}