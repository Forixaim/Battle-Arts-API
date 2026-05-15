plugins {
    alias(libs.plugins.javalib)
    alias(libs.plugins.eclipse)
    alias(libs.plugins.idea)
    alias(libs.plugins.moddevgradle)
    alias(libs.plugins.publisher)
}

val mod_version: String by project
val mod_group_id: String by project
val mod_id: String by project
val minecraft_version: String by project
val neoforge_version: String by project
val epicfight_version: String by project
val epicskills_version: String by project

val minecraft_version_range: String by project
val neoforge_version_range: String by project
val loader_version_range: String by project
val mod_name: String by project
val mod_license: String by project
val mod_authors: String by project
val mod_description: String by project


repositories {
    fun RepositoryHandler.strictMaven(url: String, repoName: String? = null, vararg groups: String) {
        exclusiveContent {
            forRepository {
                maven(url) {
                    if (repoName != null) name = repoName
                }
            }
            filter {
                groups.forEach { includeGroupAndSubgroups(it) }
            }
        }
    }

    strictMaven("https://cursemaven.com", "Curse Maven", "curse.maven")
    strictMaven("https://api.modrinth.com/maven", "Modrinth","maven.modrinth")


    flatDir {
        dir("./libs")
    }

    mavenCentral()
}

base {
    archivesName = mod_id
    version = getFullModVersion("neoforge")
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

neoForge {
    version = neoforge_version
    runs {
        create("client") {
            client()
            devLogin.set(true)
            systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
        }

        create("clientNoAuth") {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
        }

        create("server") {
            server()
            systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
        }

        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel = org.slf4j.event.Level.DEBUG
        }
    }

    mods {
        register(mod_id) {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main {
    resources.srcDir(layout.projectDirectory.dir("src/generated/resources"))
}


dependencies {
    implementation(libs.epicFight)
    implementation(libs.epicskills)
}

private fun Project.getFullModVersion(variant: String): String = "${mod_version}-mc${minecraft_version}-$variant"


val generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    val replaceProperties = mapOf(
            "minecraft_version"       to minecraft_version,
            "minecraft_version_range" to minecraft_version_range,
            "neo_version"            to neoforge_version,
            "neo_version_range"      to neoforge_version_range,
            "loader_version_range"   to loader_version_range,
            "mod_id"                 to mod_id,
            "mod_name"               to mod_name,
            "mod_license"            to mod_license,
            "mod_version"            to mod_version,
            "mod_authors"            to mod_authors,
            "mod_description"        to mod_description
    )

    inputs.properties(replaceProperties)
    expand(replaceProperties)

    from("src/main/templates")
    into("build/generated/sources/modMetadata")
}

sourceSets.main.get().resources.srcDir(generateModMetadata)

val TaskContainer.jar: TaskProvider<Jar>
    get() = named<Jar>("jar")

fun Project.extractCurrentVersionChangelog(): String? {
    val changelogFile = rootProject.file("CHANGELOG.md")
    val fullChangelogText = changelogFile.readText()

    // Extracts the current version changelog without the version heading 2 and "For Devs" heading 3.
    val versionSectionRegex =
        "(?s)## \\[$mod_version\\] - \\d{4}-\\d{2}-\\d{2}\\R(.*?)(?=\\R### For Devs|\\R## \\[.*?\\] |\\Z)"
    val matcher = Regex(versionSectionRegex).find(fullChangelogText) ?: return null

    val versionChangelog = matcher.groupValues[1]
    return versionChangelog
}

publishMods {
    val readme: File = project.file("CHANGELOG.md")
    val readmeContent: String = readme.readText()

    val versionSectionRegex = "(?s)## \\[$mod_version\\] - \\d{4}-\\d{2}-\\d{2}\\R(.*?)(?=\\R### For Devs|\\R## \\[.*?\\] |\\Z)"
    val matchResult = Regex(versionSectionRegex).find(readmeContent) ?: throw RuntimeException("No changelog found for version $mod_version in CHANGELOG.md file")

    val latestChangelog: String = matchResult.groupValues[1]
    dryRun = false

    changelog.set("""
        |$latestChangelog
        |
        |**Tested against:**
        |- **NeoForge:** $neoforge_version
        |- **Minecraft:** $minecraft_version
    """.trimMargin().trim())

    // Type of the release: ALPHA, BETA, STABLE
    type.set(STABLE)

    // The name of the file appeared in publishing websites
    displayName = getFullModVersion("neoforge")

    file.set(tasks.named<Jar>("jar").flatMap { it.archiveFile })
    modLoaders.add("neoforge")

    curseforge {
        projectId.set("1091499")
        projectSlug.set("battle-arts-api")
        accessToken.set(providers.environmentVariable("CURSEFORGE_TOKEN"))
        minecraftVersions.add(minecraft_version)

        javaVersions.add(JavaVersion.VERSION_21)

        clientRequired.set(true)
        serverRequired.set(true)
    }

    modrinth {
        projectId.set("7baP43pD")
        accessToken.set(providers.environmentVariable("MODRINTH_TOKEN"))
        minecraftVersions.add(minecraft_version)
        requires("epic-fight")
    }

    discord {
        username.set("APIMaker4000")
        webhookUrl.set(providers.environmentVariable("DISCORD_WEBHOOK"))
        avatarUrl.set("https://cdn.discordapp.com/attachments/1404959979496013894/1487715037689810945/Acid.png?ex=69ca2619&is=69c8d499&hm=22ccf65d8fee84a316d829f1f063cb45c1f53918f1b4b2fda653c3ab7203d094&")
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}
