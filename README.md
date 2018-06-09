Java 9 Concurrency Cookbook recipes in Kotlin
=============

[![TravisCI Build](https://travis-ci.org/hiper2d/java9-concurrency-cookbook.svg)](https://travis-ci.org/hiper2d/java9-concurrency-cookbook)

### Technology stack
* Gradle 4.7 with Kotlin Script
* Kotlin 1.2.41
* Kotlin Coroutines
* JMH performance tests
* jUnit 5
* Log4j2

### JMH Guide
> use `./gradlew` instead of `gradle` if you didn't installed `gradle`
```bash
# I use JMH Gradle plugin which caches compilation results between runs.
# To avoid this stop Gradle daemon before tests run.
gradle -stop

# Run performance tests for benchmarks listed in the jmh.include of the build.gradle.kts.
gradle clean jmh
```
> I also recommend to switch the root logger level in Log4j2.xml to `info`
> to get rid of internal code console outputs during performance tests.
