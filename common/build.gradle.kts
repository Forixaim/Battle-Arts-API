plugins {
    id("multiloader-base")
    alias(libs.plugins.moddevgradle)
    alias(libs.plugins.mcSafeResources)
}

configureBaseArchive("common")

neoForge {
    neoFormVersion = libs.versions.neoform.get()
}

dependencies {
    // compileOnly(libs.forgeconfigapiport.common) // Required in non-Forge like platforms deactivated until 26.1 released
    compileOnly(libs.mixin)
    compileOnly(libs.asm.tree) // Manual import for implementations of IMixinConfigPlugin

    api(libs.playerAnimationLibrary)
    api(libs.bendableCuboids)
}

mcSafeResources {
    namespace.set(modId)
    outputPackage.set("com.yesman.${modId}.generated")
}

// TODO: temporarily disabled old code, remove when the job finished
sourceSets.main.get().java {
    exclude("yesman/epicfight/**")
    exclude("ongoing/yesman/epicfight/**")
}

java.sourceSets.main.get().java.srcDirs(
    tasks.generateLangKeys.map { it.outputs.files.singleFile },
    tasks.generateSoundKeys.map { it.outputs.files.singleFile }
)

tasks.compileJava { dependsOn(tasks.generateLangKeys, tasks.generateSoundKeys) }

// The API JAR file includes only classes from the API package.

val apiPackage = "com/yesman/epicfight/api/**"
val apiJarClassifier = "api"

val apiJar by tasks.registering(Jar::class) {
    group = "build"
    archiveClassifier.set(apiJarClassifier)

    from(sourceSets.main.get().output) { include(apiPackage) }
}

val apiSourcesJar by tasks.registering(Jar::class) {
    group = "build"
    archiveClassifier.set("$apiJarClassifier-sources") // Important to use this suffix, to follow standards

    from(sourceSets.main.get().allSource) { include(apiPackage) }
}



artifacts {
    archives(apiJar)
    archives(apiSourcesJar)
}
