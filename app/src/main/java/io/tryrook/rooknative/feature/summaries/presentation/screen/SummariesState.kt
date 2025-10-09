package io.tryrook.rooknative.feature.summaries.presentation.screen

import io.tryrook.rooknative.feature.summaries.domain.model.Summary

data class SummariesState(
    val loading: Boolean = true,
    val healthConnectSummary: Summary? = null,
    val samsungHealthSummary: Summary? = null,
    val androidSummary: Summary? = null,
)
