package com.app.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.app.android.ui.RootScreen
import com.app.core.ui.theme.AndroidAppCleanTemplateTheme
import com.app.navigation.NavigationDefinition
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navDefinitions: Set<@JvmSuppressWildcards NavigationDefinition>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AndroidAppCleanTemplateTheme {
                RootScreen(definitions = navDefinitions)
            }
        }
    }
}