package io.tryrook.rooknative.feature.connections.presentation.screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ConnectionsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ConnectionsState())
    val uiState get() = _uiState.asStateFlow()
}
