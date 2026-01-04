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
 * ./gradlew generateFeature --featureName=YourFeatureName
 * ```
 *
 * To also add the feature to the bottom navigation bar, use the `addToNavBar` option:
 *
 * ```bash
 * ./gradlew generateFeature --featureName=YourFeatureName --addToNavBar
 * ```
 *
 * @property featureName The name of the feature to be generated. This is passed as a
 *                       project property (`-featureName`). For example, `-featureName=Profile`.
 * @property addToNavBar A boolean flag to indicate if the new feature should be added
 *                       to the main application's bottom navigation bar. Passed as a
 *                       command-line option (`--addToNavBar`). Defaults to `false`.
 */
abstract class GenerateFeatureTask : DefaultTask() {
    init {
        group = "buildSrc"
        description = "Generates a new feature module"
    }

    @get:Input
    @set:Option(option = "featureName", description = "Name of the feature to be generated")
    var featureName: String? = null

    @Input
    @Option(option = "addToNavBar", description = "Should feature appear in bottom bar")
    var addToNavBar: Boolean = false

    @TaskAction
    fun generate() {
        with(project) {
            val mFeatureName = featureName
                ?: throw IllegalArgumentException("Feature name must be provided via -featureName")
            val featureNameLowercased = mFeatureName.lowercase()
            val featureNameCapitalized = mFeatureName.replaceFirstChar { it.uppercase() }
            val modulePath = "${rootDir.path}/feature/$featureNameLowercased"
            val featurePackage = getFeaturePackageName(featureNameLowercased)
            val featureSrcPath = getFeatureModuleSrcPath(featureNameLowercased)

            logger.lifecycle("Generating feature module: name=$featureNameLowercased, addToNavBar=$addToNavBar")
            logger.info("Paths: modulePath=$modulePath, srcPath=$featureSrcPath, package=$featurePackage")

            createRootModuleFiles(modulePath, featureNameLowercased)
            createPackageStructure(featureSrcPath)
            createDomainPlaceholders(featureNameCapitalized, featureSrcPath, featurePackage)
            createDataPlaceholders(featureNameCapitalized, featureSrcPath, featurePackage)
            createPresentationPlaceholders(featureNameCapitalized, featureSrcPath, featurePackage)
            createDiPlaceholders(featureNameCapitalized, featureSrcPath, featurePackage)
            createNavigation(featureNameCapitalized, featureSrcPath, featurePackage)

            logger.lifecycle("Feature module generated: $featureNameLowercased. Sync the project.")
        }
    }

    private fun createRootModuleFiles(modulePath: String, featureName: String) {
        with(project) {
            logger.info("Creating root module files at: $modulePath")

            file(modulePath).mkdirs()

            file("$modulePath/.gitignore").writeText(
                Templates.gitignore()
            )
            logger.debug("Wrote: $modulePath/.gitignore")

            file("$modulePath/build.gradle.kts").apply {
                writeText(
                    Templates.gradleBuild(featureName)
                )
            }
            logger.debug("Wrote: $modulePath/build.gradle.kts")

            file("$modulePath/src/main/AndroidManifest.xml").apply {
                parentFile.mkdirs()
                writeText(Templates.androidManifest(featureName))
            }
            logger.debug("Wrote: $modulePath/src/main/AndroidManifest.xml")

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
                    logger.debug("Wrote: $modulePath/src/main/res/values/strings.xml")

                    if (addToNavBar) {
                        val sourceIconFile =
                            file("${rootDir.path}/app/src/main/res/drawable/outline_android_24.xml")
                        val targetIconFile =
                            file("$modulePath/src/main/res/drawable/outline_android_24.xml")
                        if (sourceIconFile.exists()) {
                            sourceIconFile.copyTo(targetIconFile, overwrite = true)
                            logger.debug("Copied icon to: $modulePath/src/main/res/drawable/outline_android_24.xml")
                        } else {
                            logger.warn("Nav bar icon source file not found: ${sourceIconFile.path}")
                        }
                    }
                }
            }

            file("$modulePath/proguard-rules.pro").writeText("# Proguard rules here")
            logger.debug("Wrote: $modulePath/proguard-rules.pro")

            file("$modulePath/consumer-rules.pro").writeText("# Consumer rules here")
            logger.debug("Wrote: $modulePath/consumer-rules.pro")
        }
    }

    private fun createPackageStructure(srcPath: String) {
        with(project) {
            logger.info("Creating package structure at: $srcPath")

            val dataPackages = listOf("model", "repository", "local", "remote", "mapper")
            dataPackages.forEach { pkg ->
                file("$srcPath/data/$pkg").mkdirs()
                logger.debug("Created dir: $srcPath/data/$pkg")
            }

            val domainPackages = listOf("model", "repository", "usecase")
            domainPackages.forEach { pkg ->
                file("$srcPath/domain/$pkg").mkdirs()
                logger.debug("Created dir: $srcPath/domain/$pkg")
            }

            val presentationPackages = listOf("screen", "mvi", "component", "navigation")
            presentationPackages.forEach { pkg ->
                file("$srcPath/presentation/$pkg").mkdirs()
                logger.debug("Created dir: $srcPath/presentation/$pkg")
            }
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

            val outFile = "$domainPath/repository/${featureName}Repository.kt"
            file(outFile).apply {
                writeText(
                    Templates.domainRepository(
                        featureName,
                        domainPackage
                    )
                )
            }
            logger.debug("Wrote: $outFile")
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

            val outFile = "$dataPath/repository/${featureName}RepositoryImpl.kt"
            file(outFile).apply {
                writeText(
                    Templates.dataRepository(
                        featureName,
                        dataPackage,
                        domainPackage
                    )
                )
            }
            logger.debug("Wrote: $outFile")
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

            val screenFile = "$presentationPath/screen/${featureName}Screen.kt"
            file(screenFile).apply {
                writeText(
                    Templates.screen(
                        featureName,
                        navigationPackage
                    )
                )
            }
            logger.debug("Wrote: $screenFile")

            val contractFile = "$presentationPath/mvi/${featureName}Contract.kt"
            file(contractFile).apply {
                writeText(
                    Templates.mvi(
                        featureName,
                        presentationPackage
                    )
                )
            }
            logger.debug("Wrote: $contractFile")

            val viewModelFile = "$presentationPath/mvi/${featureName}ViewModel.kt"
            file(viewModelFile).apply {
                writeText(
                    Templates.viewModel(
                        featureName,
                        presentationPackage
                    )
                )
            }
            logger.debug("Wrote: $viewModelFile")
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
            logger.debug("Created dir: $dataDiPath")

            val dataModuleFile = "$dataDiPath/${featureName}DataModule.kt"
            file(dataModuleFile).writeText(
                Templates.dataDiModule(featureName, featurePackage)
            )
            logger.debug("Wrote: $dataModuleFile")

            val presentationDiPath = "$featureSrcPath/presentation/di"
            file(presentationDiPath).mkdirs()
            logger.debug("Created dir: $presentationDiPath")

            val navModuleFile = "$presentationDiPath/${featureName}NavigationModule.kt"
            file(navModuleFile).writeText(
                Templates.navigationDiModule(featureName, featurePackage, navigationPackage)
            )
            logger.debug("Wrote: $navModuleFile")
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
            logger.debug("Created dir: $navPath")

            val routesFile = "$navPath/${featureName}Routes.kt"
            file(routesFile).writeText(
                Templates.routes(featureName, featurePackage)
            )
            logger.debug("Wrote: $routesFile")

            val navDefFile = "$navPath/${featureName}NavigationDefinition.kt"
            file(navDefFile).writeText(
                Templates.navDefinition(featureName, featurePackage, navigationPackage, addToNavBar)
            )
            logger.debug("Wrote: $navDefFile")
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