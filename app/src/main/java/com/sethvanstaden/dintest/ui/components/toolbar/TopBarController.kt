package com.sethvanstaden.dintest.ui.components.toolbar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class TopBarController {
    var state by mutableStateOf(TopBarState())
        private set

    fun set(state: TopBarState) {
        this.state = state
    }
}