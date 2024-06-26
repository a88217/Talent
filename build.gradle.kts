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
	sourceCompatibility = JavaVersion.VERSION_20
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

	implementation("org.springframework.boot:spring-boot-starter:3.1.0")
	implementation("org.springframework.boot:spring-boot-starter-web:3.1.0")
	implementation("org.springframework.boot:spring-boot-starter-validation:3.0.4")
	implementation("org.springframework.boot:spring-boot-devtools:3.0.4")
	implementation("org.springframework.boot:spring-boot-starter-security:3.0.4")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:3.1.0")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.0.4")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-mail:3.2.5")
	// https://mvnrepository.com/artifact/org.springframework.security.oauth.boot/spring-security-oauth2-autoconfigure
	implementation("org.springframework.security.oauth.boot:spring-security-oauth2-autoconfigure:2.6.8")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client:3.2.5")
	implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
	implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:3.3.0")
	implementation("org.thymeleaf:thymeleaf-spring6:3.1.2.RELEASE")



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