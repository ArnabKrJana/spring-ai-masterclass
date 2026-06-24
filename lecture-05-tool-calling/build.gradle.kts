plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

extra["springAiVersion"] = "2.0.0"

dependencyManagement {
    imports {
        mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-restclient")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    // Chat Memory
    implementation("org.springframework.ai:spring-ai-starter-model-chat-memory-repository-jdbc")
// Advisor
    implementation("org.springframework.ai:spring-ai-advisors-vector-store:2.0.0-M8")
    // LLM & Embedding Model
    implementation("org.springframework.ai:spring-ai-starter-model-ollama")

    // PGVector
//    implementation("org.springframework.ai:spring-ai-starter-vector-store-pgvector")

    // Document Readers
    implementation("org.springframework.ai:spring-ai-pdf-document-reader")
    implementation("org.springframework.ai:spring-ai-markdown-document-reader")
// Vectorstore
//    implementation("org.springframework.ai:spring-ai-starter-vector-store-redis")
    runtimeOnly("org.postgresql:postgresql")


    developmentOnly("org.springframework.boot:spring-boot-docker-compose")

    testImplementation("org.springframework.boot:spring-boot-starter-restclient-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xjsr305=strict",
            "-Xannotation-default-target=param-property"
        )
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}