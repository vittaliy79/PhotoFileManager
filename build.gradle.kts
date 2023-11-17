plugins {
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
    id("java")
}

group = "com.vitalmoments"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("commons-io:commons-io:2.15.0")
    implementation("commons-io:commons-io:2.15.0")
    implementation("org.apache.commons:commons-imaging:1.0-alpha3")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
}
/*

tasks.named("bootBuildImage") {
    builder = "paketobuildpacks/builder-jammy-base:latest"
}
*/
