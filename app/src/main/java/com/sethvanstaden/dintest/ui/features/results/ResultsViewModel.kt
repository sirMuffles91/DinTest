package com.sethvanstaden.dintest.ui.features.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sethvanstaden.dintest.domain.usecases.results.TestResultUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ResultsViewModel(
    testResultUseCase: TestResultUseCase
): ViewModel() {

    val results = testResultUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

}