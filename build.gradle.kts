plugins {
    application
    kotlin("jvm") version "2.0.0"
}

group = "com.mfitlab"
version = "1.0-SNAPSHOT"

val beamVersion: String by properties
val slf4jVersion: String by properties

repositories {
    mavenCentral()
    maven {
        url = uri("https://packages.confluent.io/maven/")
    }
}

dependencies {

    implementation("org.apache.beam:beam-sdks-java-core:$beamVersion")
    implementation("org.apache.beam:beam-sdks-java-io-google-cloud-platform:$beamVersion")
    implementation("org.slf4j:slf4j-jdk14:$slf4jVersion")

    if (project.hasProperty("dataflow-runner")) {
        runtimeOnly("org.apache.beam:beam-runners-google-cloud-dataflow-java:$beamVersion")
    } else {
        runtimeOnly("org.apache.beam:beam-runners-direct-java:$beamVersion")
    }

    testImplementation(kotlin("test"))

}

application {
    mainClass.set("com.mfitlab.etl.Application")
}

tasks.test {
    useJUnitPlatform()
}

task("execute", JavaExec::class) {
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set(System.getProperty("mainClass"))
}

kotlin {
    jvmToolchain(17)
}