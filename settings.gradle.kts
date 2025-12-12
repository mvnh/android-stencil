pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AndroidAppCleanTemplate"
include(":app")
include(":core")
include(":navigation")

val featuresDir = File(rootDir, "feature")
if (featuresDir.exists() && featuresDir.isDirectory) {
    featuresDir.listFiles()
        ?.filter { it.isDirectory && File(it, "build.gradle.kts").exists() }
        ?.forEach { featureDir ->
            include("feature:${featureDir.name}")
        }
}