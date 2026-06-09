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
    implementation(platform("de.bund.bva.isyfact:isyfact-products-bom:3.1.0"))
    implementation(platform("de.bund.bva.isyfact:isyfact-standards-bom:3.1.0"))

    // Core Spring Boot starters managed by the IsyFact Products BOM
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-jpa")


    // Logging infrastructure aligned with German Federal standard formatting
    implementation("de.bund.bva.isyfact:isy-logging")

    // Security wrapper for automated role and authorization mapping
    //implementation("de.bund.bva.isyfact:isy-security")

    // Health-check and monitoring endpoints
    implementation("de.bund.bva.isyfact:isy-ueberwachung")

    // Testing suite
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
}