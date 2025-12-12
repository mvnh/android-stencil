import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.app.buildsrc.Constants

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
        jvmTarget = JvmTarget.fromTarget("21")
    }
}
       
android {
    namespace = "com.app.feature.second"
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