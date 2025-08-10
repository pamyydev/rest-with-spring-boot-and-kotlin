import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Plugins necessários para o projeto
plugins {
    id("org.springframework.boot") version "3.2.1" // Framework principal
    id("io.spring.dependency-management") version "1.1.4" // Gerenciamento de dependências
    kotlin("jvm") version "1.9.21" // Compilador Kotlin
    kotlin("plugin.spring") version "1.9.21" // Plugin do Spring para Kotlin
    kotlin("plugin.jpa") version "1.9.21" // Plugin JPA para Kotlin (gera construtores padrão)
}

group = "com.interview"
version = "1.0.0"

// Versão do Java (17 é LTS e padrão do Spring Boot 3.x)
java {
    sourceCompatibility = JavaVersion.VERSION_17
}

// Repositórios onde buscar dependências
repositories {
    mavenCentral() // Repositório oficial do Maven
}

// Dependências do projeto
dependencies {
    // Spring Boot Starters - "kits" com dependências relacionadas
    implementation("org.springframework.boot:spring-boot-starter-web") // REST APIs
    implementation("org.springframework.boot:spring-boot-starter-data-jpa") // JPA + Hibernate
    implementation("org.springframework.boot:spring-boot-starter-validation") // Validações

    // Kotlin specific
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin") // JSON + Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect") // Reflection necessária pro Spring
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8") // Stdlib do Kotlin

    // Banco de dados
    runtimeOnly("com.h2database:h2") // H2 só é necessário em runtime
    // Para produção, descomente e configure:
    // runtimeOnly("org.postgresql:postgresql")

    // Desenvolvimento e monitoring
    developmentOnly("org.springframework.boot:spring-boot-devtools") // Hot reload
    implementation("org.springframework.boot:spring-boot-starter-actuator") // Health checks, metrics

    // Documentação da API
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0") // Swagger/OpenAPI

    // Testes
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine") // Remove JUnit 4
    }
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5") // Testes com Kotlin

    // Testes de integração
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql") // Para testes com banco real
}

// Configuração das tasks
tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict" // Null-safety mais rigorosa
        jvmTarget = "17" // Target da JVM
    }
}

tasks.withType<Test> {
    useJUnitPlatform() // Usa JUnit 5
}

// Plugin JPA do Kotlin - configurações específicas
allOpen {
    // JPA precisa que as classes sejam "open" (não final)
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

noArg {
    // JPA precisa de construtor padrão sem argumentos
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

// task customizada para rodar a aplicação em desenvolvimento
tasks.register("dev") {
    group = "application"
    description = "Roda a aplicação em modo desenvolvimento"

    doLast {
        exec {
            commandLine("./gradlew", "bootRun", "--args='--spring.profiles.active=dev'")
        }
    }
}

// configurações do Spring Boot plugin
springBoot {
    buildInfo() // gera infos de build (útil para /actuator/info)

    // Configurações para build de produção
    mainClass.set("com.interview.taskmanager.TaskManagerApplicationKt")
}