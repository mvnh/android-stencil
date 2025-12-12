package com.app.feature.second.domain.repository

import com.app.feature.second.domain.model.FeatureName
        
interface SecondRepository {
    // Define repository methods here
    
    suspend fun getFeatureName(): FeatureName
}