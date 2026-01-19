package com.sethvanstaden.dintest.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination

@Serializable
object Home: Destination

@Serializable
object Test: Destination

@Serializable
object Results: Destination