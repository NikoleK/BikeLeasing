plugins {
    kotlin("jvm") version "2.3.0"
    id("net.serenity-bdd.serenity-gradle-plugin") version "5.4.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.3")
    testImplementation("net.serenity-bdd:serenity-core:5.4.1")
    testImplementation("net.serenity-bdd:serenity-junit5:5.4.1")
    testImplementation("net.serenity-bdd:serenity-rest-assured:5.4.1")
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
    finalizedBy("aggregate")
}