package com.app.feature.example.presentation.navigation

import kotlinx.serialization.Serializable
import com.app.navigation.NavigableGraph
import com.app.navigation.NavigableRoute

@Serializable
data object ExampleGraph : NavigableGraph

@Serializable
data object ExampleRoute : NavigableRoute

// Add more routes here if needed