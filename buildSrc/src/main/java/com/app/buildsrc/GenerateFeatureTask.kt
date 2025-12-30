package com.app.buildsrc

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

/**
 * A Gradle task for scaffolding a new Android feature module.
 *
 * This task automates the creation of a standard feature module structure,
 * including directories, Gradle build files, AndroidManifest, and placeholder
 * classes for a clean architecture (Data, Domain, Presentation layers),
 * DI modules, and navigation components.
 *
 * It is designed to be run from the command line.
 *
 * ### Usage:
 *
 * To generate a new feature module, run the following command from the project's root directory:
 *
 * ```bash
 * ./gradlew generateFeature -PfName=YourFeatureName
 * ```
 *
 * To also add the feature to the bottom navigation bar, use the `addToNavBar` option:
 *
 * ```bash
 * ./gradlew generateFeature -PfName=YourFeatureName --addToNavBar
 * ```
 *
 * @property featureName The name of the feature to be generated. This is passed as a
 *                       project property (`-PfName`). For example, `-PfName=Profile`.
 * @property addToNavBar A boolean flag to indicate if the new feature should be added
 *                       to the main application's bottom navigation bar. Passed as a
 *                       command-line option (`--addToNavBar`). Defaults to `false`.
 */
abstract class GenerateFeatureTask : DefaultTask() {
    init {
        group = "buildSrc"
        description = "Generates a new feature module"
    }

    @Input
    var featureName: String? = project.findProperty("fName") as String?

    @Input
    @Option(option = "addToNavBar", description = "Should feature appear in bottom bar")
    var addToNavBar: Boolean = false

    @TaskAction
    fun generate() {
        with(project) {
            val mFeatureName = featureName
                ?: throw IllegalArgumentException("Feature name must be provided via -PfName")
            val featureNameLowercased = mFeatureName.lowercase()
            val featureNameCapitalized = mFeatureName.replaceFirstChar { it.uppercase() }
            val modulePath = "${rootDir.path}/feature/$featureNameLowercased"
            val featurePackage = getFeaturePackageName(featureNameLowercased)
            val featureSrcPath = getFeatureModuleSrcPath(featureNameLowercased)

            println("Generating feature module '$featureNameLowercased'...")

            createRootModuleFiles(modulePath, featureNameLowercased)
            createPackageStructure(featureSrcPath)
            createDomainPlaceholders(featureNameCapitalized, featureSrcPath, featurePackage)
            createDataPlaceholders(featureNameCapitalized, featureSrcPath, featurePackage)
            createPresentationPlaceholders(featureNameCapitalized, featureSrcPath, featurePackage)
            createDiPlaceholders(featureNameCapitalized, featureSrcPath, featurePackage)
            createNavigation(featureNameCapitalized, featureSrcPath, featurePackage)

            println("Feature module '$featureNameLowercased' has been generated successfully. Please sync the project.")
        }
    }

    private fun createRootModuleFiles(modulePath: String, featureName: String) {
        with(project) {
            file(modulePath).mkdirs()

            file("$modulePath/.gitignore").writeText(
                Templates.gitignore()
            )

            file("$modulePath/build.gradle.kts").apply {
                writeText(
                    Templates.gradleBuild(featureName)
                )
            }

            file("$modulePath/src/main/AndroidManifest.xml").apply {
                parentFile.mkdirs()
                writeText(Templates.androidManifest(featureName))
            }

            mkdir("$modulePath/src/main/res").apply {
                listOf(
                    "drawable",
                    "values"
                ).forEach { dir ->
                    mkdir("$modulePath/src/main/res/$dir")
                }.also {
                    file("$modulePath/src/main/res/values/strings.xml").writeText(
                        Templates.stringsXml(featureName)
                    )
                    if (addToNavBar) {
                        val sourceIconFile =
                            file("${rootDir.path}/app/src/main/res/drawable/outline_android_24.xml")
                        val targetIconFile =
                            file("$modulePath/src/main/res/drawable/outline_android_24.xml")
                        if (sourceIconFile.exists()) {
                            sourceIconFile.copyTo(targetIconFile, overwrite = true)
                        }
                    }
                }
            }

            file("$modulePath/proguard-rules.pro").writeText("# Proguard rules here")
            file("$modulePath/consumer-rules.pro").writeText("# Consumer rules here")
        }
    }

    private fun createPackageStructure(srcPath: String) {
        with(project) {
            val dataPackages = listOf("model", "repository", "local", "remote", "mapper")
            dataPackages.forEach { file("$srcPath/data/$it").mkdirs() }

            val domainPackages = listOf("model", "repository", "usecase")
            domainPackages.forEach { file("$srcPath/domain/$it").mkdirs() }

            val presentationPackages = listOf("screen", "mvi", "component", "navigation")
            presentationPackages.forEach { file("$srcPath/presentation/$it").mkdirs() }
        }
    }

    private fun createDomainPlaceholders(
        featureName: String,
        featureSrcPath: String,
        featurePackage: String
    ) {
        with(project) {
            val domainPath = "$featureSrcPath/domain"
            val domainPackage = "$featurePackage.domain"

            file("$domainPath/repository/${featureName}Repository.kt").apply {
                writeText(
                    Templates.domainRepository(
                        featureName,
                        domainPackage
                    )
                )
            }
        }
    }

    private fun createDataPlaceholders(
        featureName: String,
        featureSrcPath: String,
        featurePackage: String,
    ) {
        with(project) {
            val dataPath = "$featureSrcPath/data"
            val dataPackage = "$featurePackage.data"
            val domainPackage = "$featurePackage.domain"

            file("$dataPath/repository/${featureName}RepositoryImpl.kt").apply {
                writeText(
                    Templates.dataRepository(
                        featureName,
                        dataPackage,
                        domainPackage
                    )
                )
            }
        }
    }

    private fun createPresentationPlaceholders(
        featureName: String,
        featureSrcPath: String,
        featurePackage: String,
        navigationPackage: String = "${Constants.TOP_LEVEL_DOMAIN}.${Constants.ORG_NAME}.navigation"
    ) {
        with(project) {
            val presentationPath = "$featureSrcPath/presentation"
            val presentationPackage = "$featurePackage.presentation"

            file("$presentationPath/screen/${featureName}Screen.kt").apply {
                writeText(
                    Templates.screen(
                        featureName,
                        navigationPackage
                    )
                )
            }
            file("$presentationPath/mvi/${featureName}Contract.kt").apply {
                writeText(
                    Templates.mvi(
                        featureName,
                        presentationPackage
                    )
                )
            }
            file("$presentationPath/mvi/${featureName}ViewModel.kt").apply {
                writeText(
                    Templates.viewModel(
                        featureName,
                        presentationPackage
                    )
                )
            }
        }
    }

    private fun createDiPlaceholders(
        featureName: String,
        featureSrcPath: String,
        featurePackage: String
    ) {
        with(project) {
            val navigationPackage = "${Constants.TOP_LEVEL_DOMAIN}.${Constants.ORG_NAME}.navigation"

            val dataDiPath = "$featureSrcPath/data/di"
            file(dataDiPath).mkdirs()

            file("$dataDiPath/${featureName}DataModule.kt").writeText(
                Templates.dataDiModule(featureName, featurePackage)
            )

            val presentationDiPath = "$featureSrcPath/presentation/di"
            file(presentationDiPath).mkdirs()

            file("$presentationDiPath/${featureName}NavigationModule.kt").writeText(
                Templates.navigationDiModule(featureName, featurePackage, navigationPackage)
            )
        }
    }

    private fun createNavigation(
        featureName: String,
        featureSrcPath: String,
        featurePackage: String
    ) {
        with(project) {
            val navigationPackage = "${Constants.TOP_LEVEL_DOMAIN}.${Constants.ORG_NAME}.navigation"

            val presentationPath = "$featureSrcPath/presentation"
            val navPath = "$presentationPath/navigation"

            file(navPath).mkdirs()

            file("$navPath/${featureName}Routes.kt").writeText(
                Templates.routes(featureName, featurePackage)
            )

            file("$navPath/${featureName}NavigationDefinition.kt").writeText(
                Templates.navDefinition(featureName, featurePackage, navigationPackage, addToNavBar)
            )
        }
    }

    private fun getFeaturePackageName(featureName: String): String {
        return "${Constants.TOP_LEVEL_DOMAIN}.${Constants.ORG_NAME}.feature.${featureName}"
    }

    private fun getFeatureModuleSrcPath(featureName: String): String {
        with(project) {
            return "${rootDir.path}/feature/$featureName/src/main/kotlin/${Constants.TOP_LEVEL_DOMAIN}/${Constants.ORG_NAME}/feature/$featureName"
        }
    }
}