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
    maven(url = "https://jitpack.io")
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
    implementation("com.sun.mail:javax.mail:1.6.2")

    //Telegram
    implementation("com.github.kotlin-telegram-bot:kotlin-telegram-bot:6.0.6") {
        //exclude examples cause webhook example conflict with ktor version with we use.
        exclude(module = "dispatcher")
        exclude(module = "echo")
        exclude(module = "polls")
        exclude(module = "webhook")
    }

    //Google Sheets
    implementation("com.google.api-client:google-api-client:1.33.0")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.32.1")
    implementation("com.google.apis:google-api-services-sheets:v4-rev20210629-1.32.1")

    //test
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

//workaround for duplicate strategy issues on server after tg library adding
gradle.taskGraph.whenReady {
    allTasks
        .filter { it.hasProperty("duplicatesStrategy") } // Because it's some weird decorated wrapper that I can't cast.
        .forEach {
            it.setProperty("duplicatesStrategy", "WARN")
        }
}

tasks.create("stage") {
    dependsOn("installDist")
}