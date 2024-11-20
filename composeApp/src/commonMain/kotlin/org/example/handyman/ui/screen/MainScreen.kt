package org.example.handyman.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PowerSettingsNew
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import org.example.handyman.model.data.Tool
import org.example.handyman.model.nav.NavigationRoute
import org.example.handyman.model.viewmodel.MainViewModel
import org.example.handyman.ui.theme.PaddingConstant

@OptIn(FlowPreview::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onClick: (tool: Tool) -> Unit
) {
    val tools = viewModel.tools.collectAsState(viewModel.allTools)
    val query = viewModel.query.collectAsState()

    MainScreen(
        tools = tools.value,
        onClick = onClick,
        query = query.value,
        onQueryChange = { viewModel.onQueryChange(it) },
    )
}

@Composable
fun MainScreen(
    tools: List<Tool>,
    onClick: (tool: Tool) -> Unit,
    query: String,
    onQueryChange: (String) -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(3),
        contentPadding = PaddingValues(PaddingConstant.mediumGap),
        verticalItemSpacing = PaddingConstant.mediumGap,
        horizontalArrangement = Arrangement.spacedBy(PaddingConstant.mediumGap)
    ) {
        item(key = "search", span = StaggeredGridItemSpan.FullLine) {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "search"
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(50),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(text = "Search for tools")
                }
            )
        }

        item("no-results", span = StaggeredGridItemSpan.FullLine) {
            if (tools.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(2.4f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(
                        PaddingConstant.mediumGap, Alignment.CenterVertically
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Warning,
                        contentDescription = "warning",
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = "No tools available with the query :(",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

            }
        }

        items(tools) { tool ->
            ElevatedCard(onClick = { onClick(tool) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(PaddingConstant.mediumGap),
                    verticalArrangement = Arrangement.spacedBy(PaddingConstant.extraSmallGap)
                ) {
                    Text(
                        text = tool.path.title,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = tool.description,
                        style = MaterialTheme.typography.labelMedium,
                        minLines = 2,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Icon(
                        imageVector = tool.icon,
                        contentDescription = tool.path.title,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }

        }
    }

}