package com.app.buildsrc

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.gradle.kotlin.dsl.withGroovyBuilder
import java.io.File

abstract class RepackageProjectTask : DefaultTask() {
    init {
        group = "project setup"
        description = "Nuclear repackage: scans all directories for 'src' folders."
    }

    private val oldPrefix = "${Constants.TOP_LEVEL_DOMAIN}.${Constants.ORG_NAME}" // com.app
    private val oldAppPackage = "$oldPrefix.${Constants.APP_NAME}" // com.app.android

    @get:Input
    @set:Option(option = "newPrefix", description = "New prefix (e.g. com.newcompany)")
    var newPrefix: String? = null

    @get:Input
    @set:Option(option = "newProjectName", description = "New project name (e.g. coolapp)")
    var newProjectName: String? = null

    @TaskAction
    fun run() {
        val nPrefix = newPrefix ?: throw IllegalArgumentException("Missing --newPrefix")
        val nProjectName =
            newProjectName ?: throw IllegalArgumentException("Missing --newProjectName")
        val nAppPackage = "$nPrefix.$nProjectName"

        logger.lifecycle("Starting repackaging process...")
        logger.info("Old prefix: $oldPrefix, Old app package: $oldAppPackage")
        logger.info("New prefix: $nPrefix, New project name: $nProjectName, New app package: $nAppPackage")

        val rootDir = project.rootProject.rootDir
        val separator = File.separator

        logger.info("Replacing content in editable files...")
        rootDir.walkTopDown()
            .filter { it.isFile && isEditable(it) }
            .filter { file ->
                val path = file.absolutePath
                val isInIgnoredFolder = path.contains("${separator}.gradle${separator}") ||
                        path.contains("${separator}build${separator}")
                !isInIgnoredFolder
            }
            .forEach { file ->
                val content = file.readText()
                val newContent = content
                    .replace(oldAppPackage, nAppPackage)
                    .replace(oldPrefix, nPrefix)

                if (content != newContent) {
                    file.writeText(newContent)
                    logger.debug("Updated content in: ${file.path}")
                }
            }

        logger.info("Renaming package directories...")
        rootDir.walkTopDown()
            .filter { it.isDirectory && it.name == "src" }
            .filter { !it.path.contains(".gradle") && !it.path.contains("/build/") }
            .forEach { srcDir ->
                val moduleDir = srcDir.parentFile
                val isAppModule = moduleDir.name == "app"

                srcDir.listFiles()?.filter { it.isDirectory }?.forEach { sourceSetDir ->
                    sourceSetDir.listFiles()
                        ?.filter { it.isDirectory && it.name in setOf("java", "kotlin") }
                        ?.forEach { langDir ->
                            val oldPathOnDisk =
                                if (isAppModule) oldAppPackage else oldPrefix
                            val newPathOnDisk = if (isAppModule) nAppPackage else nPrefix

                            movePackages(langDir, oldPathOnDisk, newPathOnDisk)
                        }
                }
            }

        logger.info("Updating Constants.kt...")
        updateConstantsFile(rootDir, nPrefix, nProjectName)

        logger.lifecycle("Repackaging complete. Please sync the project.")
    }

    private fun isEditable(file: File): Boolean =
        file.extension in listOf("kt", "java", "xml", "kts", "gradle", "pro")

    private fun movePackages(langRoot: File, oldPkg: String, newPkg: String) {
        val oldPath = oldPkg.replace(".", "/")
        val newPath = newPkg.replace(".", "/")
        val oldFolder = langRoot.resolve(oldPath)
        val newFolder = langRoot.resolve(newPath)

        if (oldFolder.exists() && oldFolder.absolutePath != newFolder.absolutePath) {
            logger.debug("Moving package from ${oldFolder.path} to ${newFolder.path}")
            newFolder.parentFile.mkdirs()
            project.ant.withGroovyBuilder {
                "move"(mapOf("todir" to newFolder.path)) {
                    "fileset"(mapOf("dir" to oldFolder.path))
                }
            }
            cleanEmptyParents(oldFolder)
        }
    }

    private fun cleanEmptyParents(file: File) {
        var current = file
        while (current.parentFile != null && current.parentFile.listFiles()
                ?.isEmpty() == true
        ) {
            val parent = current.parentFile
            logger.debug("Deleting empty directory: ${parent.path}")
            parent.delete()
            current = parent
        }
    }

    private fun updateConstantsFile(rootDir: File, nPrefix: String, nAppName: String) {
        rootDir.walkTopDown()
            .filter { it.name == "Constants.kt" && it.path.contains("buildSrc") }
            .firstOrNull()?.let { file ->
                val content = file.readText()
                val newContent = content
                    .replace(
                        "const val TOP_LEVEL_DOMAIN = \".*\"".toRegex(),
                        "const val TOP_LEVEL_DOMAIN = \"${nPrefix.substringBefore(".")}\""
                    )
                    .replace(
                        "const val ORG_NAME = \".*\"".toRegex(),
                        "const val ORG_NAME = \"${nPrefix.substringAfter(".")}\""
                    )
                    .replace(
                        "const val APP_NAME = \".*\"".toRegex(),
                        "const val APP_NAME = \"$nAppName\""
                    )

                file.writeText(newContent)
                logger.info("Constants.kt updated at ${file.relativeTo(rootDir)}")
            }
    }
}