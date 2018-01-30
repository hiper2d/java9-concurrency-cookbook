import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    val jUnitPlatformPluginVersion = "1.0.2"

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.junit.platform:junit-platform-gradle-plugin:$jUnitPlatformPluginVersion")
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.2.20"
}

val hamcrestVersion by project
val junitJupiterVersion by project
val log4jVersion by project

apply {
    plugin("org.junit.platform.gradle.plugin")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jre8")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")

    testCompileOnly("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
    testImplementation("org.hamcrest:java-hamcrest:$hamcrestVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

