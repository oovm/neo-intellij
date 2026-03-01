import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML

fun properties(key: String) = providers.gradleProperty(key)
fun environment(key: String) = providers.environmentVariable(key)

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
    id("org.jetbrains.intellij.platform") version "2.2.1"
    id("org.jetbrains.changelog") version "2.2.1"
    id("org.jetbrains.qodana") version "2023.3.1"
    id("org.jetbrains.kotlinx.kover") version "0.9.1"
}

group = properties("pluginGroup").get()
version = properties("pluginVersion").get()
sourceSets["main"].java.srcDirs("src/main/gen")

// Configure project's dependencies
repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

// https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html#snapshot-release
dependencies {
    intellijPlatform {
        // https://youtrack.jetbrains.com/articles/IDEA-A-2100662608/IntelliJ-IDEA-2026.1-Latest-Builds
        intellijIdeaUltimate("261.21525.39", useInstaller = true)
        bundledPlugin("com.intellij.modules.json")
        bundledPlugin("org.toml.lang")
        bundledPlugin("org.intellij.plugins.markdown")
        // https://plugins.jetbrains.com/plugin/227-psiviewer/versions
        instrumentationTools()
        // plugin("org.intellij.scala")
        // org.intellij.scala:2024.1.20,\
        // com.jetbrains.rust:241.25989.180,\
        // net.masterthought.dlanguage:1.33.1,\
        // com.jetbrains.nim:1.5.4-223,\
        // com.falsepattern.zigbrains:14.2.0-241,\
        // org.ice1000.julia:0.4.2,\
        // Pythonid:241.17011.2,\
        // com.kishmakov.mojo:0.1.8,\
        // Valkyrie:0.1.1,\
        // wit:0.1.0
    }
}

// Set the JVM language level used to build the project.
kotlin {
    jvmToolchain(21)
}

// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    groups.empty()
    repositoryUrl = properties("pluginRepositoryUrl")
}

// Configure Gradle Kover Plugin - read more: https://github.com/Kotlin/kotlinx-kover#configuration
//koverReport {
//    defaults {
//        xml {
//            onCheck = true
//        }
//    }
//}

tasks {
    wrapper {
        gradleVersion = properties("gradleVersion").get()
    }
    patchPluginXml {
        version = properties("pluginVersion").get()
        sinceBuild = properties("pluginSinceBuild")
        untilBuild = properties("pluginUntilBuild")

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        pluginDescription = providers.fileContents(layout.projectDirectory.file("description.md")).asText.map {
            val start = "<!-- Plugin description -->"
            val end = "<!-- Plugin description end -->"

            with(it.lines()) {
                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
            }
        }

        val changelog = project.changelog // local variable for configuration cache compatibility
        // Get the latest available change notes from the changelog file
        changeNotes = properties("pluginVersion").map { pluginVersion ->
            with(changelog) {
                renderItem(
                    (getOrNull(pluginVersion) ?: getUnreleased())
                        .withHeader(false)
                        .withEmptySections(false),
                    Changelog.OutputType.HTML,
                )
            }
        }
    }

    // Configure UI tests plugin
    // Read more: https://github.com/JetBrains/intellij-ui-test-robot
//    runIdeForUiTests {
//        systemProperty("robot-server.port", "8082")
//        systemProperty("ide.mac.message.dialogs.as.sheets", "false")
//        systemProperty("jb.privacy.policy.text", "<!--999.999-->")
//        systemProperty("jb.consents.confirmation.enabled", "false")
//    }

    signPlugin {
        certificateChain = environment("CERTIFICATE_CHAIN")
        privateKey = environment("PRIVATE_KEY")
        password = environment("PRIVATE_KEY_PASSWORD")
    }
    printBundledPlugins {

    }
    publishPlugin {
        dependsOn("patchChangelog")
        token = environment("PUBLISH_TOKEN")
        // The pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
        channels = properties("pluginVersion").map {
            listOf(
                it.substringAfter('-', "").substringBefore('.').ifEmpty { "default" })
        }
    }
}
