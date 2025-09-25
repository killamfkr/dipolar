package com.example.localtvplus.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChannelRepository(private val parser: M3uParser = M3uParser()) {
    suspend fun loadChannelsFromUrl(m3uUrl: String): List<Channel> = withContext(Dispatchers.IO) {
        parser.fetchAndParse(m3uUrl)
    }
}

