package com.app.feature.second.presentation.navigation

import kotlinx.serialization.Serializable
import com.app.navigation.NavigableGraph
import com.app.navigation.NavigableRoute

@Serializable
data object SecondGraph : NavigableGraph

@Serializable
data object SecondRoute : NavigableRoute

// Add more routes here if needed