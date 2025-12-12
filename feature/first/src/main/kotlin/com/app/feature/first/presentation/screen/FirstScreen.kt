package com.app.feature.first.presentation.screen
        
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.core.ui.component.BoxWithCenterText
import com.app.feature.first.presentation.mvi.FirstViewModel
import com.app.navigation.OnNavigateTo
        
@Composable
internal fun FirstScreen(
    onNavigateTo: OnNavigateTo,
    viewModel: FirstViewModel = hiltViewModel()
) {
    // Your UI implementation here
    
    val state by viewModel.state.collectAsStateWithLifecycle()
    FirstContent(
        featureName = state.featureName
    )
}

@Composable
private fun FirstContent(featureName: String) {
    BoxWithCenterText(text = "$featureName screen")
}