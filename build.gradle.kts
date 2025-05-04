    plugins {
        java
        id("org.springframework.boot") version "3.1.4"
        id("io.spring.dependency-management") version "1.1.7"
    }

    group = "at."
    version = "0.0.1-SNAPSHOT"

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    repositories {
        mavenCentral()
    }

    dependencies {

        implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        implementation("org.springframework.boot:spring-boot-starter-web")

        implementation("org.flywaydb:flyway-core")
        implementation("org.flywaydb:flyway-mysql")

        implementation("jakarta.annotation:jakarta.annotation-api")
        implementation("jakarta.persistence:jakarta.persistence-api")
        implementation("jakarta.transaction:jakarta.transaction-api")
        implementation("mysql:mysql-connector-java:8.0.29")





        implementation("com.fasterxml.jackson.core:jackson-core:2.16.0")
        implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")


        implementation("org.modelmapper:modelmapper:3.2.0")
        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

        runtimeOnly("com.mysql:mysql-connector-j")
        runtimeOnly("org.mariadb.jdbc:mariadb-java-client")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
