package com.example.localtvplus.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.localtvplus.ui.screens.ChannelListScreen
import com.example.localtvplus.ui.screens.PlayerScreen
import com.example.localtvplus.data.Channel

sealed interface ScreenState {
    data object Channels : ScreenState
    data class Player(val channel: Channel) : ScreenState
}

@Composable
fun LocalTvApp() {
    var screenState by remember { mutableStateOf<ScreenState>(ScreenState.Channels) }

    Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
        when (val state = screenState) {
            is ScreenState.Channels -> ChannelListScreen(onPlay = { channel ->
                screenState = ScreenState.Player(channel)
            })

            is ScreenState.Player -> PlayerScreen(
                channel = state.channel,
                onBack = { screenState = ScreenState.Channels }
            )
        }
    }
}

