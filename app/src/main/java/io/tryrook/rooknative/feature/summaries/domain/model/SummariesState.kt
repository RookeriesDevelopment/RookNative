package io.tryrook.rooknative.feature.summaries.domain.model

data class SummariesState(
    val loading: Boolean = true,
    val healthConnectSummary: Summary? = null,
    val samsungHealthSummary: Summary? = null,
    val androidSummary: Summary? = null,
)
