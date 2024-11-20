package org.example.handyman.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.onClick
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.example.handyman.model.channel.SnackbarMessageChannel
import org.example.handyman.model.data.Message
import org.example.handyman.ui.theme.PaddingConstant
import java.util.UUID

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UuidScreen(coroutineScope: CoroutineScope = rememberCoroutineScope()) {
    val clipBoardManager = LocalClipboardManager.current

    var plainText by remember { mutableStateOf("") }
    var uuid by remember { mutableStateOf("") }
    LaunchedEffect(plainText) {
        if (plainText.isNotEmpty()) {
            uuid = UUID.nameUUIDFromBytes(plainText.toByteArray()).toString()
        } else {
            uuid = ""
        }
    }

    val isCopiable by derivedStateOf { uuid.isNotEmpty() }

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
                modifier = Modifier.weight(1f),
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
                    text = "UUID-v4",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                ElevatedButton(
                    onClick = {
                        uuid = UUID.randomUUID().toString()
                    },
                ) { Text(text = "Random") }
            }


        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(PaddingConstant.mediumGap),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = plainText,
                onValueChange = { plainText = it },
                minLines = 5,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.fillMaxWidth().weight(1f)
            )

            TextField(
                value = uuid,
                onValueChange = { },
                minLines = 5,
                enabled = false,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.fillMaxWidth().weight(1f).onClick {
                    if (isCopiable) {
                        clipBoardManager.setText(AnnotatedString(text = uuid))
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