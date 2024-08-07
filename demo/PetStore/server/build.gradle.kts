plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
}

group = "org.example.petstoreserver"
version = "1.0.0"
application {
    mainClass.set("org.example.petstoreserver.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["development"] ?: "false"}")
}

dependencies {
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-serialization-kotlinx-xml")
    implementation("io.ktor:ktor-server-resources")

    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)

}

tasks.register("runOkgenTool", Exec::class) {
    commandLine("java", "-jar", "C:\\ISEL\\PS\\okgentool\\demo\\okgentool.jar",
        "-s", "C:\\ISEL\\PS\\okgentool\\demo\\petstore.yaml",
        "-ts","C:\\ISEL\\PS\\okgentool\\demo\\PetStore\\server\\src\\main\\kotlin",
        "-p", "org.example.petstoreserver")
}

tasks.named("build") {
    dependsOn(tasks.named("runOkgenTool"))
}