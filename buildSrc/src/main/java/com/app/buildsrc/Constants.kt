package com.app.buildsrc

import org.gradle.api.JavaVersion

object Constants {
    const val TOP_LEVEL_DOMAIN = "com"
    const val ORG_NAME = "app"
    const val APP_NAME = "android"

    const val COMPILE_SDK = 36
    const val MIN_SDK = 29
    const val TARGET_SDK = 36

    const val JVM_VERSION = 21
    val JVM_VERSION_OBJ = JavaVersion.toVersion(JVM_VERSION)

    const val VERSION_CODE = 1
    const val VERSION_NAME = "1.0"
}