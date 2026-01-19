package com.sethvanstaden.dintest.ui.components.toolbar

data class TopBarState(
    val title: String = "",
    val showBack: Boolean = false,
    val onBack: (() -> Unit)? = null,
    val actions: List<TopBarAction> = emptyList()
)