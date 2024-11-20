package org.example.handyman.model.nav

sealed class NavigationRoute(val route: String, val title: String) {
    data object Main : NavigationRoute("main", "Handyman")
    data object Hash : NavigationRoute("hash", "Hash")
    data object Uuid : NavigationRoute("uuid", "UUID")
    data object Binary : NavigationRoute("binary", "Binary")
    data object Epoch: NavigationRoute("epoch", "Epoch")
    data object Base64: NavigationRoute("base64", "Base64")
    data object Base64Image: NavigationRoute("base64_image", "Base64 Image")
}