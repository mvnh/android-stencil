package com.app.feature.second.domain.usecase
        
import com.app.feature.second.domain.repository.SecondRepository
import javax.inject.Inject
        
class GetFeatureNameUseCase @Inject constructor(
    private val repository: SecondRepository
) {
    suspend operator fun invoke(): Result<String> {
        return try {
            val featureName = repository.getFeatureName()
            Result.success(featureName.value)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}