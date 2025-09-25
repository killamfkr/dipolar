package com.example.localtvplus.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.localtvplus.data.Channel
import com.example.localtvplus.ui.ChannelViewModel

@Composable
fun ChannelListScreen(
    onPlay: (Channel) -> Unit,
    vm: ChannelViewModel = viewModel()
) {
    var m3uUrl by remember { mutableStateOf("") }
    val state = vm.uiState.value

    Surface(Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("LocalTV+", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = m3uUrl,
                    onValueChange = { m3uUrl = it },
                    label = { Text("Enter M3U URL") },
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = { if (m3uUrl.isNotBlank()) vm.load(m3uUrl) }) {
                    Text("Load")
                }
            }

            Spacer(Modifier.height(12.dp))

            when {
                state.isLoading -> {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        CircularProgressIndicator()
                    }
                }

                state.errorMessage != null -> {
                    Text("Error: ${'$'}{state.errorMessage}", color = MaterialTheme.colorScheme.error)
                }

                state.channels.isEmpty() -> {
                    Text("No channels loaded. Provide an M3U URL.")
                }

                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(state.channels, key = { it.id }) { channel ->
                            ChannelRow(channel = channel, onPlay = onPlay)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChannelRow(channel: Channel, onPlay: (Channel) -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = channel.logoUrl,
            contentDescription = null,
            modifier = androidx.compose.ui.Modifier.height(40.dp),
            contentScale = ContentScale.Fit
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(channel.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
            channel.group?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
        }
        Button(onClick = { onPlay(channel) }) { Text("Play") }
    }
}

