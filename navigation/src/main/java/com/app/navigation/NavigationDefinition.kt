package com.app.navigation

import androidx.compose.runtime.Immutable
import androidx.navigation.NavGraphBuilder

/**
 * Defines a self-contained navigation feature, typically a nested navigation graph.
 *
 * Each implementation of this interface represents a distinct feature module
 * (e.g., Home, Profile, Settings) that can be registered with the main navigation graph.
 * It encapsulates the feature's entry point, its bottom navigation bar representation (if any),
 * and the logic for building its specific `NavGraph`.
 */

@Immutable
interface NavigationDefinition {
//    val entry: KClass<out NavigableGraph>
    val entry: NavigableGraph
    val navBarItemParams: NavBarItemParams?
    fun register(onNavigateTo: OnNavigateTo): NavGraphBuilder.() -> Unit
}