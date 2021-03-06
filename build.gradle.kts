import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import java.util.regex.Pattern.compile

plugins {
  java
  application
  id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.example"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "4.1.0"
val junitJupiterVersion = "5.7.0"

val mainVerticleName = "com.example.starter.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClass.set(launcherClassName)
}

dependencies {
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-rx-java2")
  implementation("io.vertx:vertx-pg-client")
  compileOnly("io.vertx:vertx-rx-java2:4.1.0")
  compileOnly("io.vertx:vertx-pg-client:4.1.0")
  compileOnly("io.vertx:vertx-web-api-service:4.1.0")
  compileOnly("io.vertx:vertx-core:4.1.0")
  implementation("io.vertx:vertx-web-validation:4.1.0")
  implementation("io.vertx:vertx-web")
  implementation(files("libs/postgresql-42.2.21.jar"))
  implementation(files("libs/json-20210307.jar"))
  implementation(files("libs/json-simple-1.1.1.jar"))
  implementation(files("libs/vertx-sql-common-3.9.8.jar"))
  implementation(files("libs/jackson-databind-2.11.3.jar"))
  implementation("com.fasterxml.jackson.core:jackson-databind:2.4.+")
  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
  testImplementation("org.mockito:mockito-inline:3.7.7")
  testImplementation("org.mockito:mockito-junit-jupiter:3.7.7")
  compileOnly("io.netty:netty-resolver-dns-native-macos:4.1.59.Final:osx-x86_64")
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}
