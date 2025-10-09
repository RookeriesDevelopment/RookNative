package io.tryrook.rooknative.feature.summaries.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tryrook.rooknative.di.IO
import io.tryrook.rooknative.feature.summaries.domain.usecase.GetAndroidSummaryUseCase
import io.tryrook.rooknative.feature.summaries.domain.usecase.GetHealthConnectSummaryUseCase
import io.tryrook.rooknative.feature.summaries.domain.usecase.GetSamsungHealthSummaryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummariesViewModel @Inject constructor(
    @IO private val dispatcher: CoroutineDispatcher,
    private val getHealthConnectSummaryUseCase: GetHealthConnectSummaryUseCase,
    private val getSamsungHealthSummaryUseCase: GetSamsungHealthSummaryUseCase,
    private val getAndroidSummaryUseCase: GetAndroidSummaryUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SummariesState())
    val uiState get() = _uiState.asStateFlow()

    init {
        onAction(SummariesAction.OnRefreshSummaries)
    }

    fun onAction(action: SummariesAction) {
        when (action) {
            SummariesAction.OnRefreshSummaries -> viewModelScope.launch(dispatcher) {
                _uiState.update {
                    it.copy(
                        loading = true,
                        healthConnectSummary = null,
                        samsungHealthSummary = null,
                    )
                }

                val healthConnectSummary = async { getHealthConnectSummaryUseCase() }
                val samsungHealthSummary = async { getSamsungHealthSummaryUseCase() }
                val androidSummary = async { getAndroidSummaryUseCase() }

                _uiState.update {
                    it.copy(
                        loading = false,
                        healthConnectSummary = healthConnectSummary.await(),
                        samsungHealthSummary = samsungHealthSummary.await(),
                        androidSummary = androidSummary.await(),
                    )
                }
            }
        }
    }
}
