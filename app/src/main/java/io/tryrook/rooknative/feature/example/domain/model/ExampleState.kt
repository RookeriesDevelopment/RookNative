package io.tryrook.rooknative.feature.example.domain.model

data class ExampleState(
    val loading: Boolean = false,
    val input: String = "",
    val data: List<String> = emptyList(),
)
