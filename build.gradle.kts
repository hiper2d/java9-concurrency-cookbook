import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    kotlin("jvm") version "1.3.21"
    id("me.champeau.gradle.jmh") version "0.4.6"
}

val hamcrestVersion: Any? by project
val junitJupiterVersion: Any? by project
val kotlinCoroutinesVersion: Any? by project
val log4jVersion: Any? by project

kotlin {
    experimental.coroutines = Coroutines.ENABLE
}

// JMH plugin may cache results between runs.
// Use 'gradle --stop' to avoid this.
jmh {
    include = listOf("com.hiper2d.chapter7.collections.task7.atomic.*")
    fork = 1
    iterations = 1
    timeOnIteration = "1s"
    timeout = "1s"
    warmup = "1s"
    warmupForks = 1
    warmupIterations = 1
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

repositories {
    jcenter()
}

dependencies {
    jmhImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    jmhImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")

    testImplementation("org.hamcrest:java-hamcrest:$hamcrestVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
}