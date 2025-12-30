import com.app.buildsrc.Constants
import org.jetbrains.kotlin.gradle.dsl.JvmTarget


plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.1.20"
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(Constants.JVM_VERSION.toString())
    }
}

android {
    namespace = "${Constants.BASE_PACKAGE}.navigation"
    compileSdk {
        version = release(Constants.COMPILE_SDK)
    }

    defaultConfig {
        minSdk = Constants.MIN_SDK
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
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.navigation.compose)
}
