package com.app.buildsrc

import org.gradle.api.JavaVersion

/**
 * A central repository for project-wide constants, primarily used in Gradle build scripts.
 *
 * This object consolidates configuration values such as package names, SDK versions,
 * JVM targets, and module paths to ensure consistency and ease of maintenance across
 * the entire Android project.
 */
object Constants {
    const val TOP_LEVEL_DOMAIN = "com"
    const val ORG_NAME = "app"
    const val APP_NAME = "android"
    const val BASE_PACKAGE = "$TOP_LEVEL_DOMAIN.$ORG_NAME"

    const val COMPILE_SDK = 36
    const val MIN_SDK = 29
    const val TARGET_SDK = 36

    const val JVM_VERSION = 21
    val JVM_VERSION_OBJ: JavaVersion
        get() = JavaVersion.toVersion(JVM_VERSION)

    const val VERSION_CODE = 1
    const val VERSION_NAME = "1.0"

    object Modules {
        const val CORE = ":core"
        const val NAVIGATION = ":navigation"
    }
}