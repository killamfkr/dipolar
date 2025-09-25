package com.example.localtvplus.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localtvplus.data.Channel
import com.example.localtvplus.data.ChannelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChannelUiState(
    val isLoading: Boolean = false,
    val channels: List<Channel> = emptyList(),
    val errorMessage: String? = null
)

class ChannelViewModel(
    private val repository: ChannelRepository = ChannelRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChannelUiState(isLoading = false))
    val uiState: StateFlow<ChannelUiState> = _uiState.asStateFlow()

    fun load(m3uUrl: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {
                val channels = repository.loadChannelsFromUrl(m3uUrl)
                _uiState.value = ChannelUiState(isLoading = false, channels = channels, errorMessage = null)
            } catch (t: Throwable) {
                _uiState.value = ChannelUiState(isLoading = false, channels = emptyList(), errorMessage = t.message)
            }
        }
    }
}

