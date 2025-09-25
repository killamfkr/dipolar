package com.example.localtvplus.data

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedReader
import java.io.StringReader
import java.util.UUID

class M3uParser(private val httpClient: OkHttpClient = OkHttpClient()) {
    fun parseFromText(m3uText: String): List<Channel> {
        val reader = BufferedReader(StringReader(m3uText))
        val channels = mutableListOf<Channel>()

        var currentName: String? = null
        var currentLogo: String? = null
        var currentGroup: String? = null

        reader.forEachLine { rawLine ->
            val line = rawLine.trim()
            if (line.isEmpty()) return@forEachLine

            if (line.startsWith("#EXTINF", ignoreCase = true)) {
                currentName = extractName(line)
                currentLogo = extractAttribute(line, "tvg-logo")
                currentGroup = extractAttribute(line, "group-title")
            } else if (!line.startsWith("#")) {
                val url = line
                val name = currentName ?: url.substringAfterLast('/')
                val id = UUID.nameUUIDFromBytes((name + url).toByteArray()).toString()
                channels.add(
                    Channel(
                        id = id,
                        name = name,
                        url = url,
                        logoUrl = currentLogo,
                        group = currentGroup
                    )
                )
                currentName = null
                currentLogo = null
                currentGroup = null
            }
        }

        return channels
    }

    fun fetchAndParse(url: String): List<Channel> {
        val request = Request.Builder().url(url).build()
        httpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) error("Failed to fetch M3U: ${'$'}{response.code}")
            val body = response.body?.string() ?: error("Empty body")
            return parseFromText(body)
        }
    }

    private fun extractAttribute(line: String, key: String): String? {
        val regex = Regex("${'$'}key=\"(.*?)\"")
        return regex.find(line)?.groupValues?.getOrNull(1)
    }

    private fun extractName(line: String): String? {
        val idx = line.indexOf(",")
        if (idx >= 0 && idx < line.length - 1) {
            return line.substring(idx + 1).trim()
        }
        return null
    }
}

