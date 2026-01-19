package com.sethvanstaden.dintest.ui.components.toolbar

import androidx.compose.runtime.staticCompositionLocalOf

val LocalTopBarController = staticCompositionLocalOf<TopBarController> {
    error("TopBarController not provided")
}