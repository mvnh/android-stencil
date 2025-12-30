package com.app.android.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.app.android.ui.navigation.AppNavBar
import com.app.android.ui.navigation.AppNavHost
import com.app.navigation.NavigationDefinition
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet

@Composable
internal fun RootScreen(
    navController: NavHostController = rememberNavController(),
    definitions: ImmutableSet<@JvmSuppressWildcards NavigationDefinition>
) {
    val sortedNavBarDefinitions = remember(definitions) {
        definitions
            .filter { it.navBarItemParams != null }
            .sortedBy { it.navBarItemParams!!.order }
            .toImmutableSet()
    }

    Scaffold(
        bottomBar = {
            AppNavBar(navController, sortedNavBarDefinitions)
        },
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        modifier = Modifier.semantics {
            testTagsAsResourceId = true
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AppNavHost(navController, definitions)
        }
    }
}