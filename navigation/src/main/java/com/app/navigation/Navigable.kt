package com.app.navigation

import kotlinx.serialization.Serializable

interface Navigable

@Serializable
data object PreviousScreen : Navigable