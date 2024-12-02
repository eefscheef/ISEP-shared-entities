plugins {
    kotlin("jvm")
}

group = "ut.isep"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.5")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.0")
}


tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
