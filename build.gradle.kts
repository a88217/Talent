plugins {
	id("org.springframework.boot") version "3.2.3"
	id("io.spring.dependency-management") version "1.1.4"
	application
	id("io.sentry.jvm.gradle") version "4.1.0"
	checkstyle
	jacoco
}

group = "com.circule"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}

dependencies {
	compileOnly("org.projectlombok:lombok:1.18.30")
	annotationProcessor("org.projectlombok:lombok:1.18.30")
	implementation("org.openapitools:jackson-databind-nullable:0.2.6")
	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
	runtimeOnly("com.h2database:h2")
	implementation("net.datafaker:datafaker:2.1.0")
	implementation("org.instancio:instancio-junit:3.3.1")
	implementation("com.puppycrawl.tools:checkstyle:10.12.4")
	implementation("org.postgresql:postgresql:42.7.1")

	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-devtools")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui")

	testCompileOnly("org.projectlombok:lombok:1.18.30")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation(platform("org.junit:junit-bom:5.10.0"))
	testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
	testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")
	testImplementation("net.datafaker:datafaker:2.1.0")
	testImplementation("org.instancio:instancio-junit:3.3.1")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

checkstyle {
	toolVersion = "10.3.3"
	configFile  = file("config/checkstyle/checkstyle.xml")
	isIgnoreFailures = true
	maxWarnings = 10
	maxErrors = 10
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

application {
	mainClass = "com.circule.talent.TalentApplication"
}

jacoco {
	toolVersion = "0.8.9"
	reportsDirectory = layout.buildDirectory.dir("reports/jacoco")
}

tasks.jacocoTestReport {
	reports {
		xml.required = true
		csv.required = false
		html.outputLocation = layout.buildDirectory.dir("reports/jacoco")
	}
}

sentry {
	val env = System.getenv("APP_ENV")
	if (env != null && env.contentEquals("prod")) {
		includeSourceContext = true
		org = "aleksandr-muzalev"
		projectName = "talent"
		authToken = System.getenv("SENTRY_AUTH_TOKEN")
	}
}