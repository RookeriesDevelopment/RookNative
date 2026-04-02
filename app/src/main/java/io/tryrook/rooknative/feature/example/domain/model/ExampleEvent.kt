package io.tryrook.rooknative.feature.example.domain.model

import io.tryrook.rooknative.core.presentation.text.UIText

sealed interface ExampleEvent {
    data object OnDataSubmitted : ExampleEvent
    data class OnError(val message: UIText) : ExampleEvent
}
