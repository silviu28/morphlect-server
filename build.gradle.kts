plugins {
    kotlin("jvm") version "2.2.21"
}

group = "org"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // Source: https://mvnrepository.com/artifact/io.ktor/ktor-client-core-jvm
    implementation("io.ktor:ktor-client-core-jvm:3.4.0")
}

kotlin {
    jvmToolchain(20)
}

tasks.test {
    useJUnitPlatform()
}