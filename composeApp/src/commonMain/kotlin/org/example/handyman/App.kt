package org.example.handyman

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.example.handyman.model.nav.NavigationRoute
import org.example.handyman.model.viewmodel.MainViewModel
import org.example.handyman.ui.screen.Base64Screen
import org.example.handyman.ui.screen.BinaryScreen
import org.example.handyman.ui.screen.EpochScreen
import org.example.handyman.ui.screen.HashScreen
import org.example.handyman.ui.screen.ImageBase64Screen
import org.example.handyman.ui.screen.MainScreen
import org.example.handyman.ui.screen.UuidScreen
import org.example.handyman.ui.theme.HandymanTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App(
    viewModel: MainViewModel = viewModel { MainViewModel() },
    navController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    val currentPath = navController.currentBackStackEntry?.destination?.route
    val darkMode = viewModel.darkMode ?: isSystemInDarkTheme()
    var title by remember { mutableStateOf(NavigationRoute.Main.title) }

    // snackbar message handling
    var job: Job? by remember { mutableStateOf(null) }
    LaunchedEffect(viewModel.message) {
        viewModel.message?.let {
            if (job != null) {
                job?.cancel()
                job = null
            }
            job = coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = it.message,
                    duration = SnackbarDuration.Short
                )
            }
            viewModel.resetMessage()
        }
    }

    HandymanTheme(isDarkTheme = darkMode) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = { viewModel.toggleDarkMode(!darkMode) },
                        ) {
                            Icon(
                                imageVector = if (darkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "dark mode toggle",
                            )
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    listOf(
                        Triple("Home", NavigationRoute.Main, Icons.Default.Home),
                        Triple("Back", null, Icons.AutoMirrored.Filled.Undo)
                    ).forEach {
                        NavigationBarItem(
                            selected = false,
                            onClick = {
                                it.second?.let { route ->
                                    if (route.route != currentPath) {
                                        navController.navigate(route.route)
                                        title = route.title
                                    }
                                } ?: run {
                                    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                                        navController.popBackStack()
                                        title = NavigationRoute.Main.title
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = it.third,
                                    contentDescription = it.first
                                )
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "main",
                enterTransition = { slideInHorizontally { it } },
                popEnterTransition = { slideInHorizontally { -it } },
                exitTransition = { slideOutHorizontally { -it } },
                popExitTransition = { slideOutHorizontally { it } },
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = NavigationRoute.Main.route) {
                    MainScreen(
                        viewModel = viewModel,
                        onClick = { tool ->
                            navController.navigate(tool.path.route)
                            title = tool.path.title
                        }
                    )
                }

                composable(route = NavigationRoute.Hash.route) {
                    HashScreen()
                }

                composable(route = NavigationRoute.Epoch.route) {
                    EpochScreen()
                }

                composable(route = NavigationRoute.Uuid.route) {
                    UuidScreen()
                }

                composable(route = NavigationRoute.Base64.route) {
                    Base64Screen()
                }

                composable(route = NavigationRoute.Base64Image.route) {
                    ImageBase64Screen()
                }

                composable(route = NavigationRoute.Binary.route) {
                    BinaryScreen()
                }
            }
        }
    }
}