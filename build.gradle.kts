plugins {
    application
    kotlin("jvm") version "1.9.23"
}

group = "io.github.zerumi"
version = "1.0-SNAPSHOT"

application {
    mainClass = "io.github.zerumi.MainKt"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:4.4.0")
    implementation("com.indvd00m.ascii.render:ascii-render:2.2.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
