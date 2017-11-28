buildscript {
    val kotlinVersion = "1.2.0-rc-84"

    repositories {
        mavenCentral()
        maven(url = "http://dl.bintray.com/kotlin/kotlin-eap-1.2")
    }
    dependencies {
        classpath("org.junit.platform:junit-platform-gradle-plugin:1.0.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

plugins {
    java
}

apply {
    plugin("kotlin")
}

val hamcrestVersion by project
val junitJupiterVersion by project
val log4jVersion by project

repositories {
    mavenCentral()
    maven(url = "http://dl.bintray.com/kotlin/kotlin-eap-1.2")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jre8")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")
    testCompileOnly("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
    testImplementation("org.hamcrest:java-hamcrest:$hamcrestVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
}

