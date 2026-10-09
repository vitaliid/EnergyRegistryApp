plugins {
    id("java")
    id("org.springframework.boot") version "4.1.0"
    id("org.openapi.generator") version "7.24.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

repositories {
    mavenCentral() // IsyFact libraries are available here
}

dependencies {
    // Import IsyFact Platform BOMs to enforce standardized versions
    implementation(platform("de.bund.bva.isyfact:isyfact-products-bom:5.0.2"))
    implementation(platform("de.bund.bva.isyfact:isyfact-standards-bom:5.0.2"))

    //IsyFact LIBS-----------------------------------------------------
    // Logging infrastructure aligned with German Federal standard formatting
    implementation("de.bund.bva.isyfact:isy-logging")

    // Security wrapper for automated role and authorization mapping
    //implementation("de.bund.bva.isyfact:isy-security")

    // Health-check and monitoring endpoints
    //implementation("de.bund.bva.isyfact:isy-ueberwachung")
    //-----------------------------------------------------
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-liquibase")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.3")

    //security
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    //keycloak admin api: user accounts (name, e-mail, enabled) live in Keycloak, see KeycloakUserService
    implementation("org.keycloak:keycloak-admin-client:26.0.4")

    //metrics
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.springframework.boot:spring-boot-starter-aop:3.5.3")


    //2nd level cache
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.hibernate.orm:hibernate-jcache")
    implementation("org.ehcache:ehcache")

    implementation("org.hibernate.orm:hibernate-envers")

    //Lombok
    compileOnly("org.projectlombok:lombok:1.18.46")
    annotationProcessor("org.projectlombok:lombok:1.18.46")

    // MapStruct core and processor
    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    //DB
    implementation("org.liquibase:liquibase-core")
    runtimeOnly("org.postgresql:postgresql")

    //Search
    implementation(platform("org.hibernate.search:hibernate-search-bom:8.2.2.Final"))
    implementation("org.hibernate.search:hibernate-search-mapper-orm")
    implementation("org.hibernate.search:hibernate-search-backend-lucene")

    implementation("org.apache.commons:commons-csv:1.14.1")

    implementation("org.springframework.boot:spring-boot-starter-kafka")

    implementation("io.minio:minio:8.6.0")

    implementation("de.siegmar:logback-gelf:6.1.1")

    //it is required to distinguish request value "was omitted" from "was explicitly nulled" once deserialized
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")

    // Testing suite
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")

    //testcontainers
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation(platform("org.testcontainers:testcontainers-bom:2.0.5"))
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")
    testImplementation("org.testcontainers:testcontainers-postgresql")
}

openApiGenerate {
    generatorName.set("spring")
    // The spec is copied by processResources into build/resources/main/static (see below),
    // so it is both the generator input and served as a static resource at runtime.
    inputSpec.set(layout.buildDirectory.file("resources/main/static/ApiDefinition.yaml").get().asFile.path)
    outputDir.set(layout.buildDirectory.dir("generated/openapi").get().asFile.path)

    apiPackage.set("org.example.api")
    modelPackage.set("org.example.model")

    globalProperties.set(
        mapOf(
            "models" to "",       // generate models
            "apis" to "",         // generate api interfaces
            "supportingFiles" to "false"   // <-- skip pom.xml, README.md, etc.
        )
    )

    configOptions.set(
        mapOf(
            "useSpringBoot3" to "true",
            //Uses jakarta.* package namespace instead of the older javax.*
            "useJakartaEe" to "true",
            //Controls where the @RequestMapping annotation (with the base path) is placed.
            "requestMappingMode" to "api_interface",
            //Generates only interfaces for API operations
            "interfaceOnly" to "true",
            //Keeping the interface clean with just method signatures
            "skipDefaultInterface" to "true",
            //Wraps operation return types in Spring's ResponseEntity<T> instead of returning the raw DTO
            "useResponseEntity" to "true",
            //Adds Bean Validation annotations to generated model
            "useBeanValidation" to "true",
            //Adds @Valid annotations so validation is actually triggered at runtime
            "performBeanValidation" to "true",
            //Configures the generator to produce annotations/config compatible with springdoc-openapi
            "documentationProvider" to "springdoc",
            //Omits the Generated timestamp comment/annotation that would otherwise appear at the top of generated files
            "hideGenerationTimestamp" to "true",
            //Uses java.time types (LocalDate, OffsetDateTime, etc.) for date/date-time fields, instead of legacy
            "dateLibrary" to "java8",
            //uses tags to build interfaces
            "useTags" to "true",
            //Initialises generated List/Map fields with null instead of an empty collection, so a request
            //body can distinguish "property absent" (null -> leave unchanged) from "[]" (explicitly empty).
            //Required for UserUpdate.roles, where [] means "remove all roles in scope".
            "containerDefaultToNull" to "true"
        )
    )
}

sourceSets {
    main {
        java {
            srcDir(layout.buildDirectory.dir("generated/openapi/src/main/java"))
        }
    }
}

tasks.compileJava {
    dependsOn(tasks.openApiGenerate)
}

tasks.openApiGenerate {
    dependsOn(tasks.processResources)
}

tasks.processResources {
    from("src/main/resources/api/ApiDefinition.yaml") {
        into("static")
    }
}

tasks.test {
    useJUnitPlatform()
}