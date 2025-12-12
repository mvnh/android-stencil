package com.app.feature.second.presentation.mvi
     
import androidx.lifecycle.viewModelScope
import com.app.core.extension.PatternViewModel
import com.app.feature.second.domain.usecase.GetFeatureNameUseCase
import com.app.feature.second.presentation.mvi.SecondContract.State
import com.app.feature.second.presentation.mvi.SecondContract.Intent
import com.app.feature.second.presentation.mvi.SecondContract.Intent.GetFeatureName
import com.app.feature.second.presentation.mvi.SecondContract.Effect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
        
@HiltViewModel
class SecondViewModel @Inject constructor(
    // Inject dependencies here if needed
    private val getFeatureNameUseCase: GetFeatureNameUseCase
) : PatternViewModel<State, Intent, Effect>(
    initialState = State()
) {
    init {
        onIntent(Intent.GetFeatureName)
    }
    
    private fun getFeatureName() {
        viewModelScope.launch {
            getFeatureNameUseCase().fold(
                onSuccess = { value -> reduce { copy(featureName = value) } },
                onFailure = { /* Handle error if needed */ }
            )
        }
    }

    override fun onIntent(intent: Intent) {
        when (intent) {
            // Handle intents here
            GetFeatureName -> getFeatureName()
        }
    }
}