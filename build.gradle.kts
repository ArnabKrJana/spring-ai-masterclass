plugins {
    kotlin("jvm") version "2.3.21" apply false
    kotlin("plugin.spring") version "2.3.21" apply false
    id("org.springframework.boot") version "4.1.0" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "spring-ai-masterclass"

// Pass repositories to all child modules
subprojects {
    repositories {
        mavenCentral()
//        maven { url = uri("https://repo.spring.io/milestone") }
    }
}