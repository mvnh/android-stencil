
import com.android.build.api.dsl.TestExtension
import com.app.buildsrc.Constants
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.baselineprofile)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(Constants.JVM_VERSION.toString())
    }
}

configure<TestExtension> {
    namespace = "${Constants.BASE_PACKAGE}.baselineprofile"
    compileSdk {
        version = release(Constants.COMPILE_SDK)
    }

    compileOptions {
        sourceCompatibility = Constants.JVM_VERSION_OBJ
        targetCompatibility = Constants.JVM_VERSION_OBJ
    }

    defaultConfig {
        minSdk = 28
        targetSdk = Constants.TARGET_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR,LOW-BATTERY"
    }

    targetProjectPath = ":app"
}

// This is the configuration block for the Baseline Profile plugin.
// You can specify to run the generators on a managed devices or connected devices.
baselineProfile {
    useConnectedDevices = true
}

dependencies {
    implementation(libs.androidx.junit)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.uiautomator)
    implementation(libs.androidx.benchmark.macro.junit4)
}

androidComponents {
    onVariants { v ->
        val artifactsLoader = v.artifacts.getBuiltArtifactsLoader()
        v.instrumentationRunnerArguments.put(
            "targetAppId",
            v.testedApks.map { artifactsLoader.load(it)?.applicationId }
        )
    }
}