import com.android.build.api.dsl.LibraryExtension
import com.app.buildsrc.Constants
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(Constants.JVM_VERSION.toString())
    }
}

configure<LibraryExtension> {
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
