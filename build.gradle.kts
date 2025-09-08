import com.github.gradle.node.npm.task.NpmTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.kotlin)
//    alias(libs.plugins.ktor)
    alias(libs.plugins.node.gradle)
}

version = "6.0"
val webapp = "webapp"

repositories {
    mavenCentral()
}

dependencies {
    minecraft(libs.minecraft)
    mappings(variantOf(libs.mappings) {
        classifier("v2")
    })
    modImplementation(libs.fabric.api)
    modImplementation(libs.fabric.kotlin)
    modImplementation(libs.fabric.loader)

    library(libs.eventbus)
    library(libs.ktor.cio)
    library(libs.ktor.core)
}

tasks.withType<ProcessResources>().configureEach {

    dependsOn("buildWebApp")
    from("$webapp/dist") {
        into("/static/")
    }

    inputs.property("version", project.version)
    inputs.property("loader", project.libs.versions.fabric.loader.get())
    inputs.property("minecraft", project.libs.versions.minecraft.get())
    inputs.property("fabric_api", project.libs.versions.fabric.api.get())
    inputs.property("kotlin", project.libs.versions.fabric.kotlin.get())

    filesMatching("fabric.mod.json") {
        expand(
            "version" to inputs.properties["version"],
            "loader" to inputs.properties["loader"],
            "minecraft" to inputs.properties["minecraft"],
            "fabric_api" to inputs.properties["fabric_api"],
            "kotlin" to inputs.properties["kotlin"],
        )
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 21
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

tasks.register<NpmTask>("installPackages") {
    args = listOf("install")
    workingDir = file(webapp)
    inputs.file("$webapp/package.json")
    outputs.dir("$webapp/node_modules")
}
tasks.register<NpmTask>("buildWebApp") {
    dependsOn("installPackages")
    workingDir = file(webapp)
    args = listOf("run", "build")
    inputs.files(fileTree("$webapp/src"), "$webapp/index.html", "$webapp/package.json")
    outputs.dir("$webapp/dist")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

/**
 * Adds extra dependencies which ktor needs,
 * Add any transitive dependencies here.
 */
afterEvaluate {
    configurations.runtimeClasspath.get().resolvedConfiguration.resolvedArtifacts.filter {
        val group = it.moduleVersion.id.group
        group.startsWith("io.ktor") // filter required transitive dependencies
    }.forEach { artifact ->
        val notation = "${artifact.moduleVersion.id.group}:${artifact.moduleVersion.id.name}:${artifact.moduleVersion.id.version}"
        dependencies.include(notation)
    }
}

/**
 * Only adds the dependency and none of its dependencies.
 * To add those, modify the afterEvaluate block.
 */
fun DependencyHandler.library(lib: Provider<MinimalExternalModuleDependency>) {
    val dependency = implementation(lib.get()) ?: error("Dependency was null (?)")
    include(dependency)
}
