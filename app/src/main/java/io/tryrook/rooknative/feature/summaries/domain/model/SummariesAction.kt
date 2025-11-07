package io.tryrook.rooknative.feature.summaries.domain.model

sealed interface SummariesAction {
    data object OnRefreshSummaries : SummariesAction
}
