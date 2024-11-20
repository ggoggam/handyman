package org.example.handyman.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.onClick
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.DragData
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.onExternalDrag
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.example.handyman.model.channel.SnackbarMessageChannel
import org.example.handyman.model.data.Message
import org.example.handyman.ui.theme.PaddingConstant
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.nio.Buffer
import java.util.Base64
import javax.imageio.ImageIO

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ImageBase64Screen(coroutineScope: CoroutineScope = rememberCoroutineScope()) {
    val clipBoardManager = LocalClipboardManager.current
    var isUrlEncoded by remember { mutableStateOf(false) }

    var imageBytes: ByteArray? by remember { mutableStateOf(null) }
    val base64 by derivedStateOf {
        val encoder = if (isUrlEncoded) Base64.getUrlEncoder() else Base64.getEncoder()
        imageBytes?.let { bytes ->
            encoder.encode(bytes).decodeToString()
        } ?: ""
    }
    val isCopiable by derivedStateOf { base64.isNotBlank() }
    var isDroppable by remember { mutableStateOf(true) }

    val launcher = rememberFilePickerLauncher(
        type = PickerType.Image,
    ) { file ->
        coroutineScope.launch {
            imageBytes = file?.readBytes()
        }
    }

    Column(
        modifier = Modifier.padding(PaddingConstant.horizontal).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(PaddingConstant.mediumGap)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                text = "Plain Text",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Row(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Base64",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isUrlEncoded,
                        onCheckedChange = { isUrlEncoded = !isUrlEncoded },
                    )
                    Text(
                        text = "URL Encoded",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(PaddingConstant.mediumGap),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(2.5f)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerHighest,
                        shape = MaterialTheme.shapes.large
                    )
                    .clip(MaterialTheme.shapes.large)
                    .clickable {
                        launcher.launch()
                    }
                    .onExternalDrag(
                        enabled = isDroppable,
                        onDragStart = { value ->
                            isDroppable = value.dragData is DragData.Image
                        },
                        onDragExit = { isDroppable = false },
                        onDrop = { value ->
                            isDroppable = false
                            val data = value.dragData
                            if (data is DragData.Image) {
                                isDroppable = true
                                val image = data
                                    .readImage()
                                    .toAwtImage(Density(1f), LayoutDirection.Ltr)
                                val buffered = BufferedImage(
                                    image.getWidth(null),
                                    image.getHeight(null),
                                    BufferedImage.TYPE_INT_RGB
                                )
                                val output = ByteArrayOutputStream()
                                ImageIO.write(
                                    buffered, "png", output
                                )
                                imageBytes = output.toByteArray()
                            }
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                imageBytes?.let { bytes ->
                    Image(
                        bitmap = loadImageBitmap(bytes.inputStream()),
                        contentDescription = "loaded image"
                    )
                } ?: run {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "image picker"
                    )
                }
            }

            var offset by remember { mutableStateOf(0f) }
            TextField(
                value = base64,
                onValueChange = { },
                minLines = 5,
                maxLines = 5,
                enabled = false,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .scrollable(
                        orientation = Orientation.Vertical,
                        state = rememberScrollableState { delta ->
                            offset += delta
                            delta
                        }
                    )
                    .onClick {
                        if (isCopiable) {
                            clipBoardManager.setText(AnnotatedString(text = base64))
                            coroutineScope.launch {
                                SnackbarMessageChannel.emit(
                                    Message(
                                        message = "Copied to clipboard"
                                    )
                                )
                            }
                        }
                    }
            )
        }
    }
}