package org.example.handyman.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.onClick
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.example.handyman.ui.theme.PaddingConstant

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun EpochScreen() {
    var epoch by remember { mutableStateOf("") }
    var timestamp by remember { mutableStateOf("") }

    var query = remember { MutableStateFlow("") }

    var expanded by remember { mutableStateOf(false) }
    val allTimezones = remember {
        TimeZone.availableZoneIds.toList().sorted()
    }
//    val timezones = query
//        .asStateFlow()
//        .
    var selected by remember {
        mutableStateOf(
            allTimezones.indexOfFirst {
                it == TimeZone.currentSystemDefault().id
            }
        )
    }


    LaunchedEffect(epoch) {
        timestamp = if (epoch.isNotBlank()) {
            Instant
                .fromEpochMilliseconds(epoch.toLong())
                .toLocalDateTime(TimeZone.of(allTimezones[selected]))
                .toString()
        } else ""
    }

    LaunchedEffect(timestamp) {
        epoch = if (timestamp.isNotBlank()) {
            LocalDateTime
                .parse(timestamp)
                .toInstant(TimeZone.of(allTimezones[selected]))
                .toEpochMilliseconds()
                .toString()
        } else ""
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
                text = "Epoch",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Row(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Date Time",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.fillMaxWidth(.4f))

                Column {
//                    TextField(
//                        value = query,
//                        onValueChange = { query = it },
//                        modifier = Modifier
//                            .onClick { expanded = !expanded },
//                        trailingIcon = {
//                            IconButton(
//                                onClick = { query = "" }
//                            ) {
//                                Icon(Icons.Default.Cancel, contentDescription = "clear")
//                            }
//                        }
//                    )
//                    DropdownMenuItem(
//                        text = {
//                            Text(allTimeZones[selected])
//                        },
//                        trailingIcon = {
//                            Icon(
//                                Icons.Default.ArrowDropDown,
//                                contentDescription = "dropdown"
//                            )
//                        },
//                        onClick = { expanded = !expanded }
//                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        allTimezones.forEachIndexed { idx, opt ->
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
            horizontalArrangement = Arrangement.spacedBy(PaddingConstant.mediumGap),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = epoch,
                onValueChange = {
                    epoch = it.filter { c -> c.isDigit() }.take(18)
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.weight(1f).fillMaxWidth()
            )

            TextField(
                value = timestamp,
                onValueChange = { },
                singleLine = true,
                modifier = Modifier.weight(1f).fillMaxWidth()
            )
        }
    }
}