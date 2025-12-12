package com.app.feature.first.presentation.navigation

import kotlinx.serialization.Serializable
import com.app.navigation.NavigableGraph
import com.app.navigation.NavigableRoute

@Serializable
data object FirstGraph : NavigableGraph

@Serializable
data object FirstRoute : NavigableRoute

// Add more routes here if needed