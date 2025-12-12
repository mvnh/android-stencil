package com.app.feature.first.data.repository
        
import com.app.feature.first.domain.repository.FirstRepository
import com.app.feature.first.domain.model.FeatureName
import javax.inject.Inject
        
class FirstRepositoryImpl @Inject constructor() : FirstRepository {
    // Implement repository methods here
    
    override suspend fun getFeatureName(): FeatureName {
        return FeatureName(value = "First")
    }
}