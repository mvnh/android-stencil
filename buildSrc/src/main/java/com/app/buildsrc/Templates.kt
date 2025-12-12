package com.app.buildsrc

import org.gradle.internal.extensions.stdlib.capitalized

object Templates {
    fun gitignore(): String = """
        /.gradle
        /local.properties
        /.idea
        *.iml
        /build
        /captures
        .externalNativeBuild
        .cxx
    """.trimIndent()

    fun gradleBuild(featureName: String): String = """
        import org.jetbrains.kotlin.gradle.dsl.JvmTarget
        import ${Constants.TOP_LEVEL_DOMAIN}.${Constants.ORG_NAME}.buildsrc.Constants
    
        plugins {
            alias(libs.plugins.android.library)
            alias(libs.plugins.kotlin.android)
            alias(libs.plugins.kotlin.compose)
            id("com.google.devtools.ksp")
            id("com.google.dagger.hilt.android")
            kotlin("plugin.serialization") version "2.2.0"
        }
    
        kotlin {
            compilerOptions {
                jvmTarget = JvmTarget.fromTarget("${Constants.JVM_VERSION}")
            }
        }
               
        android {
            namespace = "${Constants.TOP_LEVEL_DOMAIN}.${Constants.ORG_NAME}.feature.${featureName.replace("-", "_").lowercase()}"
            compileSdk {
                version = release(Constants.COMPILE_SDK)
            }
        
            defaultConfig {
                minSdk = Constants.MIN_SDK
                    
                ndk {
                    abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64"))
                }
            }
        
            buildTypes {
                release {
                    isMinifyEnabled = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }
                compileOptions {
                    sourceCompatibility = Constants.JVM_VERSION_OBJ
                    targetCompatibility = Constants.JVM_VERSION_OBJ
                }
                buildFeatures {
                    compose = true
                }
            }
        
            dependencies {
                implementation(libs.androidx.core.ktx)
                implementation(libs.androidx.lifecycle.runtime.ktx)
                implementation(libs.androidx.activity.compose)
                implementation(platform(libs.androidx.compose.bom))
                implementation(libs.androidx.compose.ui)
                implementation(libs.androidx.compose.ui.graphics)
                implementation(libs.androidx.compose.material3)
                implementation(libs.androidx.navigation.compose)
                implementation(libs.hilt.android)
                ksp(libs.hilt.android.compiler)
                implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)
                implementation(project(":core"))
                implementation(project(":navigation"))
            }
    """.trimIndent()

    fun androidManifest(featureName: String): String = """
        <manifest xmlns:android="http://schemas.android.com/apk/res/android"
            package="${Constants.TOP_LEVEL_DOMAIN}.${Constants.ORG_NAME}.feature.${featureName}">
        
            <application>
            </application>
        
        </manifest>
    """.trimIndent()

    fun dataRepository(
        featureName: String,
        dataPackage: String,
        domainPackage: String
    ): String = """
        package $dataPackage.repository
                
        import $domainPackage.repository.${featureName}Repository
        import $domainPackage.model.FeatureName
        import javax.inject.Inject
                
        class ${featureName}RepositoryImpl @Inject constructor() : ${featureName}Repository {
            // Implement repository methods here
            
            override suspend fun getFeatureName(): FeatureName {
                return FeatureName(value = "$featureName")
            }
        }
    """.trimIndent()

    fun domainRepository(
        featureName: String,
        domainPackage: String
    ): String = """
        package $domainPackage.repository
        
        import $domainPackage.model.FeatureName
                
        interface ${featureName}Repository {
            // Define repository methods here
            
            suspend fun getFeatureName(): FeatureName
        }
    """.trimIndent(
    )

    fun useCase(
        featureName: String,
        domainPackage: String
    ): String = """
        package $domainPackage.usecase
                
        import $domainPackage.repository.${featureName}Repository
        import javax.inject.Inject
                
        class GetFeatureNameUseCase @Inject constructor(
            private val repository: ${featureName}Repository
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
    """.trimIndent()

    fun model(
        domainPackage: String
    ): String = """
        package $domainPackage.model
                
        data class FeatureName(
            val value: String
        )
    """.trimIndent()

    fun screen(
        featureName: String,
        navigationPackage: String
    ): String = """
        package ${Constants.TOP_LEVEL_DOMAIN}.${Constants.ORG_NAME}.feature.${featureName.lowercase()}.presentation.screen
                
        import androidx.compose.runtime.Composable
        import androidx.compose.runtime.getValue
        import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
        import androidx.lifecycle.compose.collectAsStateWithLifecycle
        import ${Constants.TOP_LEVEL_DOMAIN}.${Constants.ORG_NAME}.core.ui.component.BoxWithCenterText
        import ${Constants.TOP_LEVEL_DOMAIN}.${Constants.ORG_NAME}.feature.${featureName.lowercase()}.presentation.mvi.${featureName}ViewModel
        import $navigationPackage.OnNavigateTo
                
        @Composable
        internal fun ${featureName}Screen(
            onNavigateTo: OnNavigateTo,
            viewModel: ${featureName}ViewModel = hiltViewModel()
        ) {
            // Your UI implementation here
            
            val state by viewModel.state.collectAsStateWithLifecycle()
            ${featureName}Content(
                featureName = state.featureName
            )
        }
        
        @Composable
        private fun ${featureName}Content(featureName: String) {
            BoxWithCenterText(text = "${'$'}featureName screen")
        }
    """.trimIndent()

    fun mvi(
        featureName: String,
        presentationPackage: String
    ): String = """
        package $presentationPackage.mvi
                
        import androidx.compose.runtime.Immutable

        object ${featureName}Contract {
                
            @Immutable
            data class State(
                // Define your state properties here
                val featureName: String = ""
            )
                    
            sealed interface Intent {
                // Define your intents here
                data object GetFeatureName : Intent
            }
                    
            sealed interface Effect {
                // Define your effects here
            }
        }
    """.trimIndent()

    fun viewModel(
        featureName: String,
        presentationPackage: String,
        domainPackage: String
    ): String = """
        package $presentationPackage.mvi
             
        import androidx.lifecycle.viewModelScope
        import ${Constants.TOP_LEVEL_DOMAIN}.${Constants.ORG_NAME}.core.extension.PatternViewModel
        import $domainPackage.usecase.GetFeatureNameUseCase
        import $presentationPackage.mvi.${featureName}Contract.State
        import $presentationPackage.mvi.${featureName}Contract.Intent
        import $presentationPackage.mvi.${featureName}Contract.Intent.GetFeatureName
        import $presentationPackage.mvi.${featureName}Contract.Effect
        import dagger.hilt.android.lifecycle.HiltViewModel
        import kotlinx.coroutines.launch
        import javax.inject.Inject
                
        @HiltViewModel
        class ${featureName}ViewModel @Inject constructor(
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
    """.trimIndent()

    fun routes(featureName: String, packageName: String): String = """
        package $packageName.presentation.navigation
        
        import kotlinx.serialization.Serializable
        import ${Constants.TOP_LEVEL_DOMAIN}.${Constants.ORG_NAME}.navigation.NavigableGraph
        import ${Constants.TOP_LEVEL_DOMAIN}.${Constants.ORG_NAME}.navigation.NavigableRoute
    
        @Serializable
        data object ${featureName}Graph : NavigableGraph
    
        @Serializable
        data object ${featureName}Route : NavigableRoute
        
        // Add more routes here if needed
    """.trimIndent()

    fun navDefinition(
        featureName: String,
        packageName: String,
        navigationPackage: String,
        addToNavBar: Boolean
    ): String = """
        package $packageName.presentation.navigation
    
        import androidx.navigation.NavGraphBuilder
        import androidx.navigation.compose.composable
        import androidx.navigation.compose.navigation
        import com.app.feature.${featureName.lowercase()}.R
        import $packageName.presentation.screen.${featureName}Screen
        ${if (addToNavBar) "import $navigationPackage.NavBarItemParams".trimIndent() else ""}
        import $navigationPackage.NavigationDefinition
        import $navigationPackage.OnNavigateTo
    
        class ${featureName}NavigationDefinition : NavigationDefinition {
        
            override val entry = ${featureName}Graph::class
            ${if (addToNavBar)
            """override val navBarItemParams = NavBarItemParams(
                labelResId = R.string.${featureName.lowercase()},
                // TODO: Replace with your order value
                order = Int.MAX_VALUE,
                // TODO: Replace with your own icon
                iconResId = R.drawable.outline_android_24
            )""" else """
            // Not added to navigation bar
            override val navBarItemParams = null"""
            }
        
            override fun register(onNavigateTo: OnNavigateTo): NavGraphBuilder.() -> Unit = {
                navigation<${featureName}Graph>(
                    startDestination = ${featureName}Route
                ) {
                    composable<${featureName}Route> {
                        ${featureName}Screen(onNavigateTo)
                    }
                    
                    // Add more composable destinations here if needed
                }
            }
        }
    """.trimIndent()

    fun dataDiModule(
        featureName: String,
        featurePackage: String
    ): String {
        val dataPackage = "$featurePackage.data"
        val domainPackage = "$featurePackage.domain"

        return """
            package $dataPackage.di
            
            import $dataPackage.repository.${featureName}RepositoryImpl
            import $domainPackage.repository.${featureName}Repository
            import dagger.Binds
            import dagger.Module
            import dagger.hilt.InstallIn
            import dagger.hilt.components.SingletonComponent
            import javax.inject.Singleton
            
            @Module
            @InstallIn(SingletonComponent::class)
            abstract class ${featureName}DataModule {
            
                @Binds
                @Singleton
                abstract fun bind${featureName}Repository(
                    impl: ${featureName}RepositoryImpl
                ): ${featureName}Repository
            
                companion object {
                    // Provides methods can be added here if needed in the future
                }
            }
        """.trimIndent()
    }

    fun navigationDiModule(
        featureName: String,
        featurePackage: String,
        navigationPackage: String
    ): String = """
        package $featurePackage.presentation.di
        
        import $featurePackage.presentation.navigation.${featureName}NavigationDefinition
        import $navigationPackage.NavigationDefinition
        import dagger.Module
        import dagger.Provides
        import dagger.hilt.InstallIn
        import dagger.hilt.components.SingletonComponent
        import dagger.multibindings.IntoSet
        
        @Module
        @InstallIn(SingletonComponent::class)
        object ${featureName}NavigationModule {
        
            @Provides
            @IntoSet
            fun provide${featureName}NavDefinition(): NavigationDefinition = ${featureName}NavigationDefinition()
        }
    """.trimIndent()

    fun stringsXml(featureName: String): String = """
        <resources>
            <string name="${featureName.replace("-", "_")}">${featureName.capitalized()}</string>
        </resources>
    """.trimIndent()
}