package org.example.handyman.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.onClick
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import java.math.BigInteger
import java.security.MessageDigest

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HashScreen(coroutineScope: CoroutineScope = rememberCoroutineScope()) {
    val clipBoardManager = LocalClipboardManager.current

    // hash algorithms
    val options = listOf("MD2", "MD5", "SHA-224", "SHA-256", "SHA-384", "SHA-512")
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(1) }

    // text box
    var plainText by remember { mutableStateOf("") }
    val hashText by derivedStateOf {
        if (plainText.isNotEmpty()) {
            val md = MessageDigest.getInstance(options[selected])
            BigInteger(1, md.digest(plainText.toByteArray())).toString(16).padStart(32, '0')
        } else ""
    }
    val isCopiable by derivedStateOf { hashText.isNotBlank() }

    Column(
        modifier = Modifier.padding(PaddingConstant.horizontal).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(PaddingConstant.smallGap)
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
                    text = "Hash",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(Modifier.fillMaxWidth(.6f))

                Column {
                    DropdownMenuItem(
                        text = {
                            Text(options[selected])
                        },
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "dropdown"
                            )
                        },
                        onClick = { expanded = !expanded }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        options.forEachIndexed { idx, opt ->
                            DropdownMenuItem(
                                text = { Text(opt) },
                                onClick = {
                                    selected = idx
                                    expanded = false
                                },
                                trailingIcon = {
                                    if (idx == selected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "selected"
                                        )
                                    }
                                }
                            )
                        }
                    }
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
                value = hashText,
                onValueChange = { },
                minLines = 5,
                enabled = false,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.weight(1f).fillMaxWidth().onClick {
                    if (isCopiable) {
                        clipBoardManager.setText(AnnotatedString(text = hashText))
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