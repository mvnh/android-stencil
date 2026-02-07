package com.app.buildsrc

import org.gradle.internal.extensions.stdlib.capitalized

/**
 * A singleton object that provides string templates for generating various files
 * required for a new feature module in an Android project. This includes Gradle build
 * files, Android manifests, Kotlin source files for different layers (data, domain, presentation),
 * navigation components, and resource files.
 *
 * The methods in this object typically accept a `featureName` and other relevant parameters
 * to customize the generated file content. This utility is primarily used by custom
 * Gradle tasks to automate the scaffolding of new features, ensuring consistency and
 * reducing boilerplate code.
 */
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
        import ${Constants.BASE_PACKAGE}.buildsrc.Constants
    
        plugins {
            alias(libs.plugins.android.library)
            
            alias(libs.plugins.kotlin.compose)
            alias(libs.plugins.ksp)
            alias(libs.plugins.hilt)
            alias(libs.plugins.kotlinx.serialization)
        }
    
        kotlin {
            compilerOptions {
                jvmTarget = JvmTarget.fromTarget(Constants.JVM_VERSION.toString())
            }
        }
               
        android {
            namespace = "${'$'}{Constants.BASE_PACKAGE}.feature.${
        featureName.replace(
            "-",
            "_"
        ).lowercase()
    }"
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
            implementation(libs.hilt.navigation.compose)
            ksp(libs.hilt.android.compiler)
            implementation(libs.kotlinx.collections.immutable)
            implementation(project(Constants.Modules.CORE))
            implementation(project(Constants.Modules.NAVIGATION))
        }
    """.trimIndent()

    fun androidManifest(featureName: String): String = """
        <manifest xmlns:android="http://schemas.android.com/apk/res/android"
            package="${Constants.BASE_PACKAGE}.feature.${featureName}">
        
        </manifest>
    """.trimIndent()

    fun dataRepository(
        featureName: String,
        dataPackage: String,
        domainPackage: String
    ): String = """
        package $dataPackage.repository
                
        import $domainPackage.repository.${featureName}Repository
        import javax.inject.Inject
                
        internal class ${featureName}RepositoryImpl @Inject constructor() : ${featureName}Repository {
            // Implement repository methods here
        }
    """.trimIndent()

    fun domainRepository(
        featureName: String,
        domainPackage: String
    ): String = """
        package $domainPackage.repository
                        
        internal interface ${featureName}Repository {
            // Define repository methods here
        }
    """.trimIndent(
    )

    fun screen(
        featureName: String,
        navigationPackage: String
    ): String = """
        package ${Constants.BASE_PACKAGE}.feature.${featureName.lowercase()}.presentation.screen
                
        import androidx.compose.runtime.Composable
        import androidx.compose.runtime.getValue
        import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
        import ${Constants.BASE_PACKAGE}.core.ui.screen.BaseScreen
        import androidx.lifecycle.compose.collectAsStateWithLifecycle
        import ${Constants.BASE_PACKAGE}.feature.${featureName.lowercase()}.presentation.mvi.${featureName}ViewModel
        import $navigationPackage.OnNavigateTo
                
        @Composable
        internal fun ${featureName}Screen(
            onNavigateTo: OnNavigateTo,
            viewModel: ${featureName}ViewModel = hiltViewModel()
        ) {
            BaseScreen(
                viewModel = viewModel
            ) { state, onIntent ->
                // Your screen content goes here
            }
        }
    """.trimIndent()

    fun mvi(
        featureName: String,
        presentationPackage: String
    ): String = """
        package $presentationPackage.mvi
                
        import androidx.compose.runtime.Immutable
        import ${Constants.BASE_PACKAGE}.core.ui.mvi.UiState

        internal object ${featureName}Contract {
                
            @Immutable
            data class State(
                override val isLoading: Boolean = false,
                override val error: Throwable? = null
               // Define your state properties here
            ) : UiState
                    
            sealed interface Intent {
                // Define your intents here
            }
                    
            sealed interface Effect {
                // Define your effects here
            }
        }
    """.trimIndent()

    fun viewModel(
        featureName: String,
        presentationPackage: String,
    ): String = """
        package $presentationPackage.mvi
             
        import android.content.Context
        import androidx.compose.runtime.Stable
        import ${Constants.BASE_PACKAGE}.core.ui.mvi.PatternViewModel
        import $presentationPackage.mvi.${featureName}Contract.State
        import $presentationPackage.mvi.${featureName}Contract.Intent
        import $presentationPackage.mvi.${featureName}Contract.Effect
        import dagger.hilt.android.lifecycle.HiltViewModel
        import dagger.hilt.android.qualifiers.ApplicationContext
        import javax.inject.Inject
        
        @Stable
        @HiltViewModel
        internal class ${featureName}ViewModel @Inject constructor(
            // Inject dependencies here if needed
            @ApplicationContext private val context: Context
        ) : PatternViewModel<State, Intent, Effect>(
            initialState = State()
        ) {
            override suspend fun onIntent(intent: Intent) {
                when (intent) {
                    // Handle intents here
                    else -> {}
                }
            }
        }
    """.trimIndent()

    fun routes(featureName: String, packageName: String): String = """
        package $packageName.presentation.navigation
        
        import kotlinx.serialization.Serializable
        import ${Constants.BASE_PACKAGE}.navigation.NavigableGraph
        import ${Constants.BASE_PACKAGE}.navigation.NavigableRoute
    
        @Serializable
        data object ${featureName}Graph : NavigableGraph
    
        @Serializable
        internal data object ${featureName}Route : NavigableRoute
        
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
        import ${Constants.BASE_PACKAGE}.feature.${featureName.lowercase()}.R
        import $packageName.presentation.screen.${featureName}Screen
        ${if (addToNavBar) "import $navigationPackage.NavBarItemParams".trimIndent() else ""}
        import $navigationPackage.NavigationDefinition
        import $navigationPackage.OnNavigateTo
    
        class ${featureName}NavigationDefinition : NavigationDefinition {
        
            override val entry = ${featureName}Graph
            ${
        if (addToNavBar)
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
            internal abstract class ${featureName}DataModule {
            
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
        internal object ${featureName}NavigationModule {
        
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