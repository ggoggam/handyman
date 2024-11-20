package org.example.handyman.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.onClick
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.example.handyman.model.channel.SnackbarMessageChannel
import org.example.handyman.model.data.Message
import org.example.handyman.model.data.SourceTargetSwitch
import org.example.handyman.ui.theme.PaddingConstant
import java.util.Base64

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Base64Screen(coroutineScope: CoroutineScope = rememberCoroutineScope()) {
    val clipBoardManager = LocalClipboardManager.current
    // flag
    var isUrlEncoded by remember { mutableStateOf(false) }

    // input
    var plainText by remember { mutableStateOf("") }
    val base64 by derivedStateOf {
        if (plainText.isNotEmpty()) {
            val encoder = if (isUrlEncoded) Base64.getUrlEncoder() else Base64.getEncoder()
            encoder.encode(plainText.toByteArray()).decodeToString()
        } else ""
    }
    val copiable by derivedStateOf { base64.isNotEmpty() }

    // index
    var switch by remember { mutableStateOf(SourceTargetSwitch.SOURCE_TO_TARGET) }

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
            TextField(
                value = plainText,
                onValueChange = { plainText = it },
                minLines = 5,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.weight(1f).fillMaxWidth()
            )

            TextField(
                value = base64,
                onValueChange = { },
                minLines = 5,
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
                    .verticalScroll(rememberScrollState())
                    .onClick {
                    if (copiable) {
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