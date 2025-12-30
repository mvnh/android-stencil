package com.app.feature.example.presentation.screen
        
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.app.core.ui.screen.BaseScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.feature.example.presentation.mvi.ExampleViewModel
import com.app.navigation.OnNavigateTo
        
@Composable
internal fun ExampleScreen(
    onNavigateTo: OnNavigateTo,
    viewModel: ExampleViewModel = hiltViewModel()
) {
    BaseScreen(
        viewModel = viewModel
    ) { state, onIntent ->
        // Your screen content goes here
    }
}