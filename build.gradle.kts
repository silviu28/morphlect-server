plugins {
    kotlin("jvm") version "2.2.21"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.21"
}

group = "org"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-content-negotiation:3.4.0")
    implementation("io.ktor:ktor-server-core:3.4.0")
    implementation("io.ktor:ktor-server-core:3.4.0")
    implementation("io.ktor:ktor-serialization-gson:3.4.0")
    implementation("io.ktor:ktor-server-content-negotiation:3.4.0")
    implementation("io.ktor:ktor-server-core:3.4.0")
    testImplementation(kotlin("test"))
    // Source: https://mvnrepository.com/artifact/io.ktor/ktor-client-core-jvm
    implementation("io.ktor:ktor-client-core-jvm:3.4.0")
    // Source: https://mvnrepository.com/artifact/io.ktor/ktor-server-core-jvm
    implementation("io.ktor:ktor-server-core-jvm:3.4.0")
    // Source: https://mvnrepository.com/artifact/io.ktor/ktor-server-netty-jvm
    implementation("io.ktor:ktor-server-netty-jvm:3.4.0")

    implementation("org.jetbrains.exposed:exposed-core:0.48.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.48.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.48.0")
    implementation("org.xerial:sqlite-jdbc:3.44.1.0")

    // Source: https://mvnrepository.com/artifact/io.ktor/ktor-client-content-negotiation
    runtimeOnly("io.ktor:ktor-client-content-negotiation:3.4.0")
    // Source: https://mvnrepository.com/artifact/io.ktor/ktor-serialization
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.7")
}

kotlin {
    jvmToolchain(20)
}

tasks.test {
    useJUnitPlatform()
}