package com.app.navigation

import androidx.navigation.NavGraphBuilder
import kotlin.reflect.KClass

interface NavigationDefinition {
    val entry: KClass<out NavigableGraph>
    val navBarItemParams: NavBarItemParams?
    fun register(onNavigateTo: OnNavigateTo): NavGraphBuilder.() -> Unit
}