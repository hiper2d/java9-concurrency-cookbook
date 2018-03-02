import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlin_version: String by extra

buildscript {
    var kotlin_version: String by extra
    kotlin_version = "1.2.21"
    val jUnitPlatformPluginVersion = "1.0.2"

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.junit.platform:junit-platform-gradle-plugin:$jUnitPlatformPluginVersion")
        classpath(kotlinModule("gradle-plugin", kotlin_version))
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.2.30"
}

val hamcrestVersion by project
val junitJupiterVersion by project
val log4jVersion by project

apply {
    plugin("org.junit.platform.gradle.plugin")
    plugin("kotlin")
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
    compile(kotlinModule("stdlib-jdk8", kotlin_version))
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}