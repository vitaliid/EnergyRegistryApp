plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral() // IsyFact libraries are available here
}

dependencies {
    // Import IsyFact Platform BOMs to enforce standardized versions
    implementation(platform("de.bund.bva.isyfact:isyfact-products-bom:5.0.2"))
    implementation(platform("de.bund.bva.isyfact:isyfact-standards-bom:5.0.2"))

    // Core Spring Boot starters managed by the IsyFact Products BOM
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    compileOnly("org.projectlombok:lombok:1.18.46")
    annotationProcessor("org.projectlombok:lombok:1.18.46")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.3")

    runtimeOnly("org.postgresql:postgresql")

    //IsyFact LIBS-----------------------------------------------------

    // Logging infrastructure aligned with German Federal standard formatting
    implementation("de.bund.bva.isyfact:isy-logging")

    // Security wrapper for automated role and authorization mapping
    //implementation("de.bund.bva.isyfact:isy-security")

    // Health-check and monitoring endpoints
    //implementation("de.bund.bva.isyfact:isy-ueberwachung")
    //-----------------------------------------------------

    // Testing suite
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
}