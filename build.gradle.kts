plugins {
    kotlin("jvm")
}

group = "ut.isep"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.1")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.18.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
}


tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
