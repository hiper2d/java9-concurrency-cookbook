import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val hamcrestVersion: Any? by project
val junitJupiterVersion: Any? by project
val log4jVersion: Any? by project

plugins {
    kotlin("jvm") version "1.2.40"
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

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }
}