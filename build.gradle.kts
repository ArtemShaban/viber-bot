val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.5.20"
}

group = "com.example"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
}

dependencies {
    //http server
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")

    //http client
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")

    //logging
    implementation("io.github.microutils:kotlin-logging:1.7.9")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    //JSON parser
    implementation("com.beust:klaxon:5.5")

    //Email
    implementation("org.apache.commons:commons-email:1.5")
    implementation("javax.mail:mail:1.4.7")

    //test
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.create("stage") {
    dependsOn("installDist")
}