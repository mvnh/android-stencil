package com.app.feature.second.presentation.screen
        
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.core.ui.component.BoxWithCenterText
import com.app.feature.second.presentation.mvi.SecondViewModel
import com.app.navigation.OnNavigateTo
        
@Composable
internal fun SecondScreen(
    onNavigateTo: OnNavigateTo,
    viewModel: SecondViewModel = hiltViewModel()
) {
    // Your UI implementation here
    
    val state by viewModel.state.collectAsStateWithLifecycle()
    SecondContent(
        featureName = state.featureName
    )
}

@Composable
private fun SecondContent(featureName: String) {
    BoxWithCenterText(text = "$featureName screen")
}