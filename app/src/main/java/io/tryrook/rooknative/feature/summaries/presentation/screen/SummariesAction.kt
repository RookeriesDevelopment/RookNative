package io.tryrook.rooknative.feature.summaries.presentation.screen

sealed interface SummariesAction {
    data object OnRefreshSummaries : SummariesAction
}
