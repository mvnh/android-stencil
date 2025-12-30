import com.app.buildsrc.Constants
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
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
    namespace = "${Constants.BASE_PACKAGE}.feature.example"
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